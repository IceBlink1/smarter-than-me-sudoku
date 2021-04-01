package com.smarterthanmesudokuapp.ui.fragments.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeArguments(val sudoku: List<List<Int>>) : Parcelable