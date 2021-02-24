package com.smarterthanmesudokuapp

import android.app.Application
import android.content.SharedPreferences
import com.smarterthanmesudokuapp.api.AuthApi
import com.smarterthanmesudokuapp.api.SudokuApi
import com.smarterthanmesudokuapp.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SudokuApplication : DaggerApplication(), HasAndroidInjector {

    var prefs: SharedPreferences? = null
    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8075")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        sudokuApi = retrofit.create(SudokuApi::class.java)
        authApi = retrofit.create(AuthApi::class.java)
        prefs = applicationContext.getSharedPreferences("app", 0)
        token = prefs?.getString(TOKEN_CACHE_KEY, null)
    }

    override fun onTerminate() {
        prefs?.edit()?.putString(TOKEN_CACHE_KEY, token)?.apply()
        super.onTerminate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }


    companion object {
        lateinit var sudokuApi: SudokuApi
        lateinit var authApi: AuthApi
        var token: String? = null
        private const val TOKEN_CACHE_KEY = "token"
    }
}