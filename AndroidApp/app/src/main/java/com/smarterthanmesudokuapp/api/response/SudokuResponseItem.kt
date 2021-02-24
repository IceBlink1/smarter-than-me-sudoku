package com.smarterthanmesudokuapp.api.response

data class SudokuResponseItem(
    val authorUserId: Int,
    val originalSudoku: String,
    val solution: String
)