package com.smarterthanmesudokuapp.domain.mappers

import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.domain.entities.SudokuVo

class SudokuMapper {
    fun mapSudoku(sudoku: Sudoku): SudokuVo {
        return SudokuVo(
            sudoku = stringSudokuTo2DArray(sudoku.originalSudoku),
            solution = stringSudokuTo2DArray(sudoku.solution),
            complexity = sudoku.complexity
        )
    }

    private fun stringSudokuTo2DArray(sudoku: String?): List<List<Int>> {
        if (sudoku == null || sudoku.length != 81) {
            throw IllegalArgumentException()
        }
        val answer = mutableListOf<List<Int>>()
        for (i in 0..9) {
            val tmp = mutableListOf<Int>()
            for (j in 0..9) {
                tmp.add(sudoku[j + i * 9].toInt())
            }
            answer.add(tmp)
        }
        return answer
    }
}