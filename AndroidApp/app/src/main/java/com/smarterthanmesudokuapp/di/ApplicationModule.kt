package com.smarterthanmesudokuapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.smarterthanmesudokuapp.data.SudokuDataMapper
import com.smarterthanmesudokuapp.data.SudokuDataSource
import com.smarterthanmesudokuapp.data.local.SudokuDB
import com.smarterthanmesudokuapp.data.local.SudokuLocalDataSource
import com.smarterthanmesudokuapp.data.remote.*
import com.smarterthanmesudokuapp.domain.repository.auth.AuthRepository
import com.smarterthanmesudokuapp.domain.repository.auth.DefaultAuthRepository
import com.smarterthanmesudokuapp.domain.repository.sudoku.DefaultSudokuRepository
import com.smarterthanmesudokuapp.domain.repository.sudoku.SudokuRepository
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
    fun provideSudokuRemoteDataSource(
        sudokuApi: SudokuApi,
        authRepository: AuthRepository,
        mapper: SudokuDataMapper
    ): SudokuDataSource {
        return SudokuRemoteDataSource(
            sudokuApi,
            authRepository = authRepository,
            mapper = mapper
        )
    }

    @Singleton
    @SudokuLocalDataSource
    @Provides
    fun provideSudokuLocalDataSource(
        database: SudokuDB,
        ioDispatcher: CoroutineDispatcher,
        mapper: SudokuDataMapper
    ): SudokuDataSource {
        return SudokuLocalDataSource(
            database.sudokuDao(),
            ioDispatcher,
            mapper
        )
    }

    @Singleton
    @Provides
    fun provideAuthDataSource(
        api: AuthApi,
        sharedPreferences: SharedPreferences
    ): AuthDataSource {
        return DefaultAuthDataSource(
            api,
            sharedPreferences = sharedPreferences
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

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    private const val PREF_FILE_NAME = "prefs.prfs"
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindSudokuRepository(repo: DefaultSudokuRepository): SudokuRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(repo: DefaultAuthRepository): AuthRepository
}