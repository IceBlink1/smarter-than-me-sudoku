package com.smarterthanmesudokuapp.ui.fragments.home

import android.os.Parcelable
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeArguments(val sudoku: SudokuVo) : Parcelable