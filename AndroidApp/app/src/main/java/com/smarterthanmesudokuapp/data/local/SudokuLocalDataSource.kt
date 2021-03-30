package com.smarterthanmesudokuapp.data.local

import javax.inject.Inject

class SudokuLocalDataSource @Inject constructor(
    sudokuDao: SudokuDao,
    mapper: SudokuLocalMapper
) {

    suspend fun postSudokus() {

    }
}