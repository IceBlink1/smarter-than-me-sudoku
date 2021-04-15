package com.smarterthanmesudokuapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SudokuDao {

    @Query("SELECT * FROM sudoku")
    suspend fun getAll(): List<SudokuDto>

    @Query("SELECT * FROM sudoku WHERE id IN (:sudokuIds)")
    suspend fun loadAllByIds(sudokuIds: IntArray): List<SudokuDto>

    @Query("SELECT * FROM sudoku WHERE id = :sudokuId")
    suspend fun getSudokuById(sudokuId: Long): SudokuDto?

    @Insert
    suspend fun insertAll(vararg sudokus: SudokuDto)

    @Query("DELETE FROM sudoku WHERE id = :sudokuId")
    suspend fun delete(sudokuId: Long)

    @Query("DELETE FROM sudoku")
    suspend fun deleteAll()

}