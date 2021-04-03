package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.data.local.SudokuDB
import com.smarterthanmesudokuapp.data.local.SudokuDao
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.SudokuDataMapper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
class LocalModule {

    @Singleton
    @Provides
    fun providesSudokuDao(demoDatabase: SudokuDB): SudokuDao {
        return demoDatabase.sudokuDao()
    }

    @Singleton
    @Provides
    fun providesSudokuRepository(
        dispatcher: CoroutineDispatcher,
        sudokuDao: SudokuDao,
        sudokuDataMapper: SudokuDataMapper
    ): SudokuLocalDataSource {
        return SudokuLocalDataSource(sudokuDao, dispatcher, sudokuDataMapper)
    }

    @Singleton
    @Provides
    fun providesSudokuLocalMapper(): SudokuDataMapper {
        return SudokuDataMapper()
    }

}