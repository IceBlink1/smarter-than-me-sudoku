package com.smarterthanmesudokuapp.data.local

import com.smarterthanmesudokuapp.data.entities.Sudoku

class SudokuLocalMapper {
    fun toDomainModel(sudokuDto: SudokuDto): Sudoku? {
        return sudokuDto.originalSudoku?.let {
            Sudoku(
                originalSudoku = it,
                solution = sudokuDto.solution,
                complexity = sudokuDto.complexity
            )
        }
    }
}