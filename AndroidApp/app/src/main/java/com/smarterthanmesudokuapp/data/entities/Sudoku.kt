package com.smarterthanmesudokuapp.data.entities

data class Sudoku(val id: Long, val originalSudoku: String, val currentSudoku: String?, val solution: String?, val complexity: Int?)