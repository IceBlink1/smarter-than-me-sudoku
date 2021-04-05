package com.smarterthanmesudokuapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SudokuVo(
    val sudoku: List<List<Int>>,
    val solution: List<List<Int>>,
    val currentSudoku: List<List<Int>>,
    val complexity: Int?,
    val showSolutionGroup: Boolean = false
) : Parcelable