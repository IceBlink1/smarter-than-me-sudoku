package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.data.local.SudokuDB
import com.smarterthanmesudokuapp.data.local.SudokuDao
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.local.SudokuLocalMapper
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
        sudokuLocalMapper: SudokuLocalMapper
    ): SudokuLocalDataSource {
        return SudokuLocalDataSource(sudokuDao, dispatcher, sudokuLocalMapper)
    }

    @Singleton
    @Provides
    fun providesSudokuLocalMapper(): SudokuLocalMapper {
        return SudokuLocalMapper()
    }

}