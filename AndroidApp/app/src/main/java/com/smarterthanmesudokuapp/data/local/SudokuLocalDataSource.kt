package com.smarterthanmesudokuapp.data.local

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.entities.Sudoku
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SudokuLocalDataSource @Inject constructor(
    val sudokuDao: SudokuDao,
    val dispatcher: CoroutineDispatcher,
    val mapper: SudokuLocalMapper
) : SudokuDataSource {

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