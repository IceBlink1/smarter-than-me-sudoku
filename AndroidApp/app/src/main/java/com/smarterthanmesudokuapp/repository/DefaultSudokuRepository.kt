package com.smarterthanmesudokuapp.repository

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.remote.SudokuRemoteDataSource
import com.smarterthanmesudokuapp.di.ApplicationModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultSudokuRepository @Inject constructor(
    @ApplicationModule.SudokuLocalDataSource val sudokuLocalDataSource: SudokuDataSource,
    @ApplicationModule.SudokuRemoteDataSource val sudokuRemoteDataSource: SudokuDataSource,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SudokuRepository {
    override suspend fun getSudokus(forceUpdate: Boolean): Result<List<Sudoku>> {
        TODO("Not yet implemented")
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