package com.smarterthanmesudokuapp.ui.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SudokuVo(
    val sudoku: List<List<Int>>,
    val solution: List<List<Int>>?,
    val currentSudoku: List<List<Int>>?,
    val complexity: Int?,
) : Parcelable