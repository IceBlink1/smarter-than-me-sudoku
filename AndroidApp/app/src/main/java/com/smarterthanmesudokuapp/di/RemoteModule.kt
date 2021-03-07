package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.data.remote.AuthApi
import com.smarterthanmesudokuapp.data.remote.SudokuApi
import com.smarterthanmesudokuapp.data.remote.SudokuRemoteDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RemoteModule {

    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl("http://localhost:8075")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    fun providesSudokuApi(retrofit: Retrofit): SudokuApi {
        return retrofit.create(SudokuApi::class.java)
    }

    @Provides
    fun providesAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun providesSudokuRemoteDataSource(sudokuApi: SudokuApi): SudokuRemoteDataSource {
        return SudokuRemoteDataSource(sudokuApi)
    }

}