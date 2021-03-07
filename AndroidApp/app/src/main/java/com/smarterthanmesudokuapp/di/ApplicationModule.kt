package com.smarterthanmesudokuapp.di

import android.content.Context
import androidx.room.Room
import com.smarterthanmesudokuapp.data.local.SudokuDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ApplicationModule {
    @Singleton
    @Provides
    fun provideDataBase(context: Context): SudokuDB {
        return Room.databaseBuilder(
            context.applicationContext,
            SudokuDB::class.java,
            "sudoku.db"
        ).build()
    }
}