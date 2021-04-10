package com.smarterthanmedigits.server.response

data class SudokuResponse(
    val remoteId: Long,
    val originalSudoku: String?,
    val solution: String?,
    val currentSudoku: String?,
    val authorUserId: Long?,
    val complexity: Int?
)