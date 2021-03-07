package com.smarterthanmesudokuapp.repository

import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.remote.SudokuRemoteDataSource
import javax.inject.Inject

class SudokuRepository @Inject constructor(
    private val localDataSource: SudokuLocalDataSource,
    private val sudokuRemoteDataSource: SudokuRemoteDataSource
) {

}
