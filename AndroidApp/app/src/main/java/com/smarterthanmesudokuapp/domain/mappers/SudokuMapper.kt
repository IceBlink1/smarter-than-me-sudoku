package com.smarterthanmesudokuapp.domain.mappers

import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.domain.entities.SudokuVo
import com.smarterthanmesudokuapp.utils.FuncUtils.getStringSudoku

class SudokuMapper {
    fun mapSudoku(sudoku: Sudoku): SudokuVo {
        return SudokuVo(
            sudoku = stringSudokuTo2DArray(sudoku.originalSudoku),
            solution = sudoku.solution?.let { stringSudokuTo2DArray(it) },
            currentSudoku = if (sudoku.currentSudoku != null)
                stringSudokuTo2DArray(sudoku.currentSudoku)
            else stringSudokuTo2DArray(
                sudoku.originalSudoku
            ),
            complexity = sudoku.complexity
        )
    }

    fun mapSudokuVo(sudokuVo: SudokuVo): Sudoku {
        return Sudoku(
            0,
            originalSudoku = sudokuVo.sudoku.getStringSudoku(),
            currentSudoku = sudokuVo.currentSudoku.getStringSudoku(),
            solution = sudokuVo.solution?.getStringSudoku(),
            complexity = sudokuVo.complexity
        )
    }

    private fun stringSudokuTo2DArray(sudoku: String): List<List<Int>> {
        if (sudoku.length != 81) {
            throw IllegalArgumentException()
        }
        val answer = mutableListOf<List<Int>>()
        for (i in 0..8) {
            val tmp = mutableListOf<Int>()
            for (j in 0..8) {
                tmp.add(sudoku[j + i * 9].toString().toInt())
            }
            answer.add(tmp)
        }
        return answer
    }
}