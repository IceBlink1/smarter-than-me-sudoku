package com.smarterthanmesudokuapp.domain.repository.sudoku

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.domain.entities.Sudoku
import com.smarterthanmesudokuapp.di.ApplicationModule
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultSudokuRepository @Inject constructor(
    @ApplicationModule.SudokuLocalDataSource val sudokuLocalDataSource: SudokuDataSource,
    @ApplicationModule.SudokuRemoteDataSource val sudokuRemoteDataSource: SudokuDataSource,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SudokuRepository {
    private var cache: ConcurrentMap<Long, Sudoku>? = null

    override suspend fun getSudokus(forceUpdate: Boolean): Result<List<Sudoku>> {
        return withContext(dispatcher) {
            if (!forceUpdate) {
                cache?.let { cache ->
                    return@withContext Success(cache.values.toList())
                }
            }

            val newSudokus = fetchSudokusFromRemoteOrLocal(forceUpdate)

            (newSudokus as? Success)?.let { refreshCache(it.data) }

            cache?.values?.let { sudokus ->
                return@withContext Success(sudokus.toList())
            }

            (newSudokus as? Success)?.let {
                if (it.data.isEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(Exception("Illegal state"))

        }
    }

    private fun refreshCache(sudokus: List<Sudoku>) {
        cache?.clear()
        sudokus.forEach {
            cacheAndPerform(it) {}
        }
    }

    private inline fun cacheAndPerform(sudoku: Sudoku, perform: (Sudoku) -> Unit) {
        val cachedSuduoku = cacheSudoku(sudoku)
        perform(cachedSuduoku)
    }

    private fun cacheSudoku(sudoku: Sudoku): Sudoku {
        val cachedSudoku =
            Sudoku(
                sudoku.id,
                sudoku.originalSudoku,
                sudoku.currentSudoku,
                sudoku.solution,
                sudoku.complexity
            )
        // Create if it doesn't exist.
        if (cache == null) {
            cache = ConcurrentHashMap()
        }
        cache?.put(cachedSudoku.id, cachedSudoku)
        return cachedSudoku
    }

    private suspend fun fetchSudokusFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Sudoku>> {
        // Remote first
        when (val remoteSudokus = sudokuRemoteDataSource.getSudokus()) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteSudokus.data)
                return remoteSudokus
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localSudokus = sudokuLocalDataSource.getSudokus()
        if (localSudokus is Success) return localSudokus
        return Error(Exception("Error fetching from remote and local"))
    }

    private suspend fun refreshLocalDataSource(sudokus: List<Sudoku>) {
        sudokuLocalDataSource.deleteAllSudokus()
        for (sudoku in sudokus) {
            sudokuLocalDataSource.saveSudoku(sudoku)
        }
    }

    override suspend fun getSudoku(sudokuId: Long, forceUpdate: Boolean): Result<Sudoku> {
        return withContext(dispatcher) {
            // Respond immediately with cache if available
            if (!forceUpdate) {
                getSudokuById(sudokuId)?.let {
                    return@withContext Success(it)
                }
            }

            val newSudoku = fetchSudokuFromRemoteOrLocal(sudokuId, forceUpdate)

            // Refresh the cache with the new tasks
            (newSudoku as? Success)?.let { cacheSudoku(it.data) }

            return@withContext newSudoku as Result<Sudoku>
        }
    }

    private suspend fun fetchSudokuFromRemoteOrLocal(
        id: Long,
        forceUpdate: Boolean
    ): Result<Sudoku> {
        // Remote first
        when (val remoteSudoku = sudokuRemoteDataSource.getSudoku(sudokuId = id)) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteSudoku.data)
                return remoteSudoku
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Refresh failed"))
        }

        // Local if remote fails
        val localTasks = sudokuLocalDataSource.getSudoku(sudokuId = id)
        if (localTasks is Success) return localTasks
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun getSudokuById(id: Long) = cache?.get(id)

    private suspend fun refreshLocalDataSource(sudoku: Sudoku) {
        sudokuLocalDataSource.saveSudoku(sudoku)
    }

    override suspend fun saveSudoku(sudoku: Sudoku) {
        cacheAndPerform(sudoku) {
            coroutineScope {
                launch {
                    sudokuRemoteDataSource.saveSudoku(it)
                }
                launch { sudokuLocalDataSource.saveSudoku(it) }
            }
        }
    }

    override suspend fun deleteAllSudokus() {
        coroutineScope {
            launch { sudokuRemoteDataSource.deleteAllSudokus() }
            launch { sudokuLocalDataSource.deleteAllSudokus() }
        }
        cache?.clear()
    }

    override suspend fun deleteSudoku(sudokuId: Long) {
        coroutineScope {
            launch { sudokuRemoteDataSource.deleteSudoku(sudokuId) }
            launch { sudokuLocalDataSource.deleteSudoku(sudokuId) }
        }

        cache?.remove(sudokuId)
    }

}