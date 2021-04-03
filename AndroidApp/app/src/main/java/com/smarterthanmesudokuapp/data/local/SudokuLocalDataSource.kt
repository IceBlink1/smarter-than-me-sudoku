package com.smarterthanmesudokuapp.data.local

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.SudokuDataMapper
import com.smarterthanmesudokuapp.data.entities.Sudoku
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SudokuLocalDataSource @Inject constructor(
    val sudokuDao: SudokuDao,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    val mapper: SudokuDataMapper
) : SudokuDataSource {

    override suspend fun getSudokus(forceUpdate: Boolean): Result<List<Sudoku>> {
        return withContext(dispatcher) {
            return@withContext try {
                Success(sudokuDao.getAll().mapNotNull { mapper.toDomainModel(it) })
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    override suspend fun getSudoku(sudokuId: Long, forceUpdate: Boolean): Result<Sudoku> {
        return withContext(dispatcher) {
            return@withContext try {
                Success(mapper.toDomainModel(sudokuDao.getSudokuById(sudokuId)!!)!!)
            } catch (e: Exception) {
                Error(e)
            }
        }
    }

    override suspend fun saveSudoku(sudoku: Sudoku) = withContext(dispatcher) {
        sudokuDao.insertAll(
            mapper.toDtoModel(sudoku)
        )
    }

    override suspend fun deleteAllSudokus() = withContext(dispatcher) {
        sudokuDao.deleteAll()
    }

    override suspend fun deleteSudoku(sudokuId: Long) = withContext(dispatcher) {
        sudokuDao.delete(sudokuId)
    }
}