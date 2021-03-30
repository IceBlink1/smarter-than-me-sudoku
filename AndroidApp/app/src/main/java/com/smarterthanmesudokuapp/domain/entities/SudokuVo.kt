package com.smarterthanmesudokuapp.domain.entities

data class SudokuVo(
    val sudoku: List<List<Int>>,
    val solution: List<List<Int>>,
    val complexity: Int?,
    val showSolutionGroup: Boolean = false
)