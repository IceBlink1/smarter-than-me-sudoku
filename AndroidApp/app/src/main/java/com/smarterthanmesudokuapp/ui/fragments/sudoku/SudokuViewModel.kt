package com.smarterthanmesudokuapp.ui.fragments.sudoku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.data.Result.Success
import com.smarterthanmesudokuapp.data.Result.Error
import com.smarterthanmesudokuapp.ui.entities.SudokuVo
import com.smarterthanmesudokuapp.domain.mappers.SudokuMapper
import com.smarterthanmesudokuapp.domain.repository.sudoku.SudokuRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SudokuViewModel @Inject constructor(
    val mapper: SudokuMapper,
    val sudokuRepository: SudokuRepository
) : ViewModel() {

    private val sudokuMutableLiveData: MutableLiveData<List<SudokuVo>> = MutableLiveData()

    val sudokuLiveData: LiveData<List<SudokuVo>>
        get() = sudokuMutableLiveData

    fun getSudokus() {
        viewModelScope.launch {
            val sudokus = sudokuRepository.getSudokus(forceUpdate = true)
            if (sudokus is Success) {
                sudokuMutableLiveData.postValue(sudokus.data.map { mapper.mapSudoku(it) })
            } else if (sudokus is Error) {
                Timber.e(sudokus.exception)
            }
        }
    }

}