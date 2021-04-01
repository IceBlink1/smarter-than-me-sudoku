package com.smarterthanmesudokuapp.di

import android.content.Context
import androidx.room.Room
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.local.SudokuDB
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.local.SudokuLocalMapper
import com.smarterthanmesudokuapp.data.remote.SudokuApi
import com.smarterthanmesudokuapp.data.remote.SudokuRemoteDataSource
import com.smarterthanmesudokuapp.repository.DefaultSudokuRepository
import com.smarterthanmesudokuapp.repository.SudokuRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SudokuRemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class SudokuLocalDataSource

    @Singleton
    @SudokuRemoteDataSource
    @Provides
    fun provideTasksRemoteDataSource(sudokuApi: SudokuApi): SudokuDataSource {
        return SudokuRemoteDataSource(
            sudokuApi
        )
    }

    @Singleton
    @SudokuLocalDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: SudokuDB,
        ioDispatcher: CoroutineDispatcher,
        mapper: SudokuLocalMapper
    ): SudokuDataSource {
        return SudokuLocalDataSource(
            database.sudokuDao(),
            ioDispatcher,
            mapper
        )
    }

    @Singleton
    @Provides
    fun provideDataBase(context: Context): SudokuDB {
        return Room.databaseBuilder(
            context.applicationContext,
            SudokuDB::class.java,
            "sudoku.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultSudokuRepository): SudokuRepository
}