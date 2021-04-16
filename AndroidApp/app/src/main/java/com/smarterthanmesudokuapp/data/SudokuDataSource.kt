package com.smarterthanmesudokuapp.data

import com.smarterthanmesudokuapp.domain.entities.Sudoku

interface SudokuDataSource {
    suspend fun getSudokus(forceUpdate: Boolean = false): Result<List<Sudoku>>

    suspend fun getSudoku(sudokuId: Long, forceUpdate: Boolean = false): Result<Sudoku>

    suspend fun saveSudoku(sudoku: Sudoku)

    suspend fun deleteAllSudokus()

    suspend fun deleteSudoku(sudokuId: Long)
}