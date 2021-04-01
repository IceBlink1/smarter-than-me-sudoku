package com.smarterthanmesudokuapp.repository

import com.smarterthanmesudokuapp.data.Result
import com.smarterthanmesudokuapp.data.entities.Sudoku

interface SudokuRepository {

    suspend fun getSudokus(forceUpdate: Boolean = false): Result<List<Sudoku>>

    suspend fun getSudoku(sudokuId: Long, forceUpdate: Boolean = false): Result<Sudoku>

    suspend fun saveSudoku(sudoku: Sudoku)

    suspend fun deleteAllSudokus()

    suspend fun deleteSudoku(sudokuId: Long)
}