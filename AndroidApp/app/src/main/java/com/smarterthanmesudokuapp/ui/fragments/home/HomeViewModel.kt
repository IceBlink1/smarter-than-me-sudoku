package com.smarterthanmesudokuapp.ui.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.smarterthanmesudokuapp.domain.mappers.SudokuMapper
import com.smarterthanmesudokuapp.repository.sudoku.SudokuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solver.Solver
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val sudokuRepository: SudokuRepository,
    private val mapper: SudokuMapper
) : ViewModel() {

    val solutionLiveData: MutableLiveData<List<List<Int>>> = MutableLiveData()

    val stepLiveData: MutableLiveData<Int> = MutableLiveData()

    fun getSolution(sudoku: List<List<Int>>) {
        viewModelScope.launch {
            val solution = withContext(Dispatchers.Default) {
                Solver.solve(sudoku.flatten(), 0)
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