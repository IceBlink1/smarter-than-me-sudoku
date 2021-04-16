package com.smarterthanmesudokuapp.ui.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarterthanmesudokuapp.ui.entities.SudokuVo
import com.smarterthanmesudokuapp.domain.mappers.SudokuMapper
import com.smarterthanmesudokuapp.domain.repository.sudoku.SudokuRepository
import exception.SudokuException
import exception.Pair
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import solver.Solver
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val sudokuRepository: SudokuRepository,
    private val mapper: SudokuMapper
) : ViewModel() {

    val solutionLiveData: MutableLiveData<List<List<Int>>> = MutableLiveData()

    val stepLiveData: MutableLiveData<Int> = MutableLiveData()

    val errorsLiveData: MutableLiveData<Set<Pair<Int, Int>>> = MutableLiveData()

    fun getSolution(sudoku: List<List<Int>>) {
        val myHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
            if (throwable is SudokuException)
                errorsLiveData.postValue(throwable.errors)
        }
        viewModelScope.launch {
            val solution = withContext(Dispatchers.Default + myHandler) {
                try {
                    Solver.solve(sudoku.flatten(), 1)
                } catch (e: SudokuException) {
                    errorsLiveData.postValue(e.errors)
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
                    if (sudoku.flatten().contains(0)) {
                        var a: Int
                        do {
                            a = rand.nextInt(81)
                        } while (sudoku.flatten()[a] != 0)
                        return@withContext a
                    } else {
                        -1
                    }
                } else {
                    return@withContext -1
                }
            }
            stepLiveData.postValue(step)
        }
    }

    fun saveSudoku(sudokuVo: SudokuVo) {
        viewModelScope.launch {
            if (sudokuVo.complexity == null)
                sudokuRepository.saveSudoku(
                    mapper.mapSudokuVo(
                        sudokuVo.copy(complexity = Solver.difficultyEstimation())
                    )
                )
            else
                sudokuRepository.saveSudoku(mapper.mapSudokuVo(sudokuVo))
        }
    }

    companion object {
        val rand = Random()
    }

}