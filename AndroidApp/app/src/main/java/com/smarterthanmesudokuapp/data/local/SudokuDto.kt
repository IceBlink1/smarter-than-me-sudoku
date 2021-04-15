package com.smarterthanmesudokuapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sudoku")
data class SudokuDto(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "original_sudoku") val originalSudoku: String?,
    @ColumnInfo(name = "current_sudoku") val currentSudoku: String?,
    @ColumnInfo(name = "solution") val solution: String?,
    @ColumnInfo(name = "complexity") val complexity: Int?,
    @ColumnInfo(name = "remote_id") val remoteId: Long?
)