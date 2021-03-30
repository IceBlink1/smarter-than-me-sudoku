package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.data.local.SudokuDB
import com.smarterthanmesudokuapp.data.local.SudokuDao
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.local.SudokuLocalMapper
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
    fun providesProductRepository(
        sudokuDao: SudokuDao,
        sudokuLocalMapper: SudokuLocalMapper
    ): SudokuLocalDataSource {
        return SudokuLocalDataSource(sudokuDao, sudokuLocalMapper)
    }

    @Singleton
    @Provides
    fun providesSudokuLocalMapper(): SudokuLocalMapper {
        return SudokuLocalMapper()
    }

}