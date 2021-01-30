package com.smarterthanmedigits.server.repository

import com.smarterthanmedigits.server.model.Sudoku
import org.springframework.data.jpa.repository.JpaRepository

interface SudokuRepository : JpaRepository<Sudoku?, Long?> {
    fun findBySolution(solution: String)
}