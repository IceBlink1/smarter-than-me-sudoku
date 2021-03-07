package com.smarterthanmesudokuapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SudokuDao {

    @Query("SELECT * FROM sudoku")
    fun getAll(): List<SudokuDto>

    @Query("SELECT * FROM sudoku WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<SudokuDto>

    @Insert
    fun insertAll(vararg users: SudokuDto)

    @Delete
    fun delete(user: SudokuDto)

}