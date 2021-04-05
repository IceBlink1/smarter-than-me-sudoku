package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.SudokuDataMapper
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.repository.auth.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SudokuRemoteDataSource @Inject constructor(
    private val sudokuApi: SudokuApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val authRepository: AuthRepository,
    private val mapper: SudokuDataMapper
) : SudokuDataSource {

    private val token: String? by lazy { "Bearer_" + authRepository.getCachedToken() }

    override suspend fun getSudokus(forceUpdate: Boolean): Result<List<Sudoku>> {
        return withContext(dispatcher) {
            if (token == null) {
                return@withContext Error(Exception("Error: token is null"))
            }
            val rsp = sudokuApi.getUserSudokus(token!!)
            val body = rsp.body()
            if (rsp.isSuccessful && body != null) {
                return@withContext Success(body.map { mapper.toDomainModel(it) })
            } else {
                return@withContext Error(
                    Exception(
                        "Error code: ${rsp.code()}.\nMessage: ${rsp.message()}"
                    )
                )
            }
        }
    }

    override suspend fun getSudoku(sudokuId: Long, forceUpdate: Boolean): Result<Sudoku> {
        return withContext(dispatcher) {
            if (token == null) {
                return@withContext Error(Exception("Error: token is null"))
            }
            val rsp = sudokuApi.getUserSudoku(token!!, sudokuId)
            val body = rsp.body()
            if (rsp.isSuccessful && body != null) {
                return@withContext Success(mapper.toDomainModel(body))
            } else {
                return@withContext Error(
                    Exception(
                        "Error code: ${rsp.code()}.\nMessage: ${rsp.message()}"
                    )
                )
            }

        }
    }

    override suspend fun saveSudoku(sudoku: Sudoku) {
        return withContext(dispatcher) {
            if (token == null) {
                return@withContext
            }
            sudokuApi.postSudokus(token!!, listOf(mapper.toPostModel(sudoku)))
        }
    }

    override suspend fun deleteAllSudokus() {
        return withContext(dispatcher) {
            if (token == null) {
                return@withContext
            }
            sudokuApi.deleteAllSudokus(token!!)
        }
    }

    override suspend fun deleteSudoku(sudokuId: Long) = withContext(dispatcher) {
        if (token == null) {
            return@withContext
        }
        sudokuApi.deleteSudokuById(token!!, sudokuId)
    }

}
