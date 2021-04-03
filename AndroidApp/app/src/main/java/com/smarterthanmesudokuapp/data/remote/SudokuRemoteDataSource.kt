package com.smarterthanmesudokuapp.data.remote

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.SudokuDataMapper
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.repository.AuthRepository
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

    var token: String? = null

    override suspend fun getSudokus(forceUpdate: Boolean): Result<List<Sudoku>> {
        return withContext(dispatcher) {
            if (token == null) {
                token = authRepository.getCachedToken()
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
        TODO("Not yet implemented")
    }

    override suspend fun saveSudoku(sudoku: Sudoku) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllSudokus() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSudoku(sudokuId: Long) {
        TODO("Not yet implemented")
    }

}
