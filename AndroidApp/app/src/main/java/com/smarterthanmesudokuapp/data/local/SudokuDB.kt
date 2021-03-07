package com.smarterthanmesudokuapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SudokuDto::class], version = 1, exportSchema = false)
abstract class SudokuDB : RoomDatabase() {
    abstract fun sudokuDao(): SudokuDao
}