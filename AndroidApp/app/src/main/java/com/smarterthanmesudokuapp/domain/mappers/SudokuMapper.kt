package com.smarterthanmesudokuapp.domain.mappers

import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.domain.entities.SudokuVo

class SudokuMapper {
    fun mapSudoku(sudoku: Sudoku): SudokuVo? {
        return if (sudoku.solution != null) SudokuVo(
            sudoku = stringSudokuTo2DArray(sudoku.originalSudoku),
            solution = stringSudokuTo2DArray(sudoku.solution),
            currentSudoku = if (sudoku.currentSudoku != null)
                stringSudokuTo2DArray(sudoku.currentSudoku)
            else stringSudokuTo2DArray(
                sudoku.originalSudoku
            ),
            complexity = sudoku.complexity
        ) else null
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