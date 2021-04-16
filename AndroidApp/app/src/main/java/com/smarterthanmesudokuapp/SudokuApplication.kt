package com.smarterthanmesudokuapp

import com.smarterthanmesudokuapp.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication

class SudokuApplication : DaggerApplication(), HasAndroidInjector {


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(applicationContext)
    }
}