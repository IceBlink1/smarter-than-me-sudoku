package com.smarterthanmesudokuapp.data.remote.post

import com.google.gson.annotations.SerializedName

data class SudokuPost(
    @SerializedName("sudoku") val originalSudoku: String,
    @SerializedName("solution") val solution: String?,
    @SerializedName("currentSudoku") val currentSudoku: String?,
    @SerializedName("complexity") val complexity: Int?
)
