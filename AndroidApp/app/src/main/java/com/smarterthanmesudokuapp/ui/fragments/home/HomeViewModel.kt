package com.smarterthanmesudokuapp.ui.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.ui.entities.SudokuVo
import com.smarterthanmesudokuapp.domain.mappers.SudokuMapper
import com.smarterthanmesudokuapp.domain.repository.sudoku.SudokuRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solver.Solver
import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val sudokuRepository: SudokuRepository,
    private val mapper: SudokuMapper
) : ViewModel() {

    val solutionLiveData: MutableLiveData<List<List<Int>>> = MutableLiveData()

    val stepLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getSolution(sudoku: List<List<Int>>) {
        val myHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
        }
        viewModelScope.launch {
            val solution = withContext(Dispatchers.Default + myHandler) {
                try {
                    Solver.solve(sudoku.flatten(), 1)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }
            solutionLiveData.postValue(solution)
        }
    }

    fun getNextStep(sudoku: List<List<Int>>) {
        viewModelScope.launch {
            val step = withContext(Dispatchers.Default) {
                val solution = solutionLiveData.value
                if (solution != null) {
                    return@withContext sudoku.flatten().indexOfFirst { it == 0 }
                } else {
                    return@withContext -1
                }
            }
            stepLiveData.postValue(step)
        }
    }

    fun saveSudoku(sudokuVo: SudokuVo) {
        viewModelScope.launch {
            sudokuRepository.saveSudoku(mapper.mapSudokuVo(sudokuVo))
        }
    }

}