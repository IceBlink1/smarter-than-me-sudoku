package com.smarterthanmesudokuapp.data

import com.smarterthanmesudokuapp.data.entities.Sudoku
import com.smarterthanmesudokuapp.data.local.SudokuDto
import com.smarterthanmesudokuapp.data.remote.response.SudokuResponseItem

class SudokuDataMapper {
    fun toDomainModel(sudokuDto: SudokuDto): Sudoku? {
        return sudokuDto.originalSudoku?.let {
            Sudoku(
                id = sudokuDto.id,
                originalSudoku = sudokuDto.originalSudoku,
                solution = sudokuDto.solution,
                complexity = sudokuDto.complexity
            )
        }
    }

    fun toDomainModel(sudokuResponseItem: SudokuResponseItem): Sudoku {
        return Sudoku(
            id = 0,
            originalSudoku = sudokuResponseItem.originalSudoku,
            solution = sudokuResponseItem.solution,
            complexity = sudokuResponseItem.complexity
        )
    }

    fun toDtoModel(sudoku: Sudoku): SudokuDto {
        return SudokuDto(
            id = 0,
            originalSudoku = sudoku.originalSudoku,
            solution = sudoku.solution,
            complexity = sudoku.complexity,
            remoteId = null
        )
    }
}