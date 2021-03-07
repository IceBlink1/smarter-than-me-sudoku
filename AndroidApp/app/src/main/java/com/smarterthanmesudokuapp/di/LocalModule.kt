package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.data.local.SudokuDB
import com.smarterthanmesudokuapp.data.local.SudokuDao
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class LocalModule {

    @Singleton
    @Provides
    fun providesProductDao(demoDatabase: SudokuDB): SudokuDao {
        return demoDatabase.sudokuDao()
    }

    @Singleton
    @Provides
    fun providesProductRepository(sudokuDao: SudokuDao): SudokuLocalDataSource {
        return SudokuLocalDataSource(sudokuDao)
    }
}