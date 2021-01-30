package com.smarterthanmedigits.server.dto

import lombok.Data

@Data
data class SudokuDto(val sudoku: String, val solution: String?)