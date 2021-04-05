package com.smarterthanmesudokuapp.data.remote.response

data class SudokuResponseItem(
    val authorUserId: Int,
    val originalSudoku: String,
    val solution: String?,
    val currentSudoku: String?,
    val complexity: Int
)