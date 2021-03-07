package com.smarterthanmedigits.server.mappers

import com.smarterthanmedigits.server.model.Sudoku
import com.smarterthanmedigits.server.response.SudokuResponse
import org.springframework.stereotype.Component

@Component
class SudokuMapper {

    fun mapModelToResponse(sudoku: Sudoku): SudokuResponse {
        return SudokuResponse(
                originalSudoku = sudoku.originalSudoku,
                solution = sudoku.solution,
                authorUserId = sudoku.user?.id
        )
    }

}