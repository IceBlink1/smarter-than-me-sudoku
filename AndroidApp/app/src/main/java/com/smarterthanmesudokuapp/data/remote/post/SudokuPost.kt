package com.smarterthanmesudokuapp.data.remote.post

import com.google.gson.annotations.SerializedName

data class SudokuPost(
    @SerializedName("originalSudoku") val originalSudoku: String,
    @SerializedName("solution") val solution: String?,
    @SerializedName("complexity") val complexity: Int?
)
