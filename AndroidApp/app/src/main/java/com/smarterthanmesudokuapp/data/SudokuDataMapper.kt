package com.smarterthanmesudokuapp.data

import com.smarterthanmesudokuapp.domain.entities.Sudoku
import com.smarterthanmesudokuapp.data.local.SudokuDto
import com.smarterthanmesudokuapp.data.remote.post.SudokuPost
import com.smarterthanmesudokuapp.data.remote.response.SudokuResponseItem

class SudokuDataMapper {
    fun toDomainModel(sudokuDto: SudokuDto): Sudoku? {
        return sudokuDto.originalSudoku?.let {
            Sudoku(
                id = sudokuDto.id,
                originalSudoku = sudokuDto.originalSudoku,
                solution = sudokuDto.solution,
                complexity = sudokuDto.complexity,
                currentSudoku = sudokuDto.currentSudoku
            )
        }
    }

    fun toDomainModel(sudokuResponseItem: SudokuResponseItem): Sudoku {
        return Sudoku(
            id = sudokuResponseItem.remoteId,
            originalSudoku = sudokuResponseItem.originalSudoku,
            solution = sudokuResponseItem.solution,
            complexity = sudokuResponseItem.complexity,
            currentSudoku = sudokuResponseItem.currentSudoku
        )
    }

    fun toPostModel(sudoku: Sudoku): SudokuPost {
        return SudokuPost(
            originalSudoku = sudoku.originalSudoku,
            solution = sudoku.solution,
            currentSudoku = sudoku.currentSudoku,
            complexity = sudoku.complexity
        )
    }

    fun toDtoModel(sudoku: Sudoku): SudokuDto {
        return SudokuDto(
            id = sudoku.id,
            originalSudoku = sudoku.originalSudoku,
            solution = sudoku.solution,
            complexity = sudoku.complexity,
            remoteId = null,
            currentSudoku = sudoku.currentSudoku
        )
    }
}