package com.smarterthanmesudokuapp.di

import android.content.Context
import com.smarterthanmesudokuapp.SudokuApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        HomeModule::class,
        ActivityModule::class,
        ApplicationModule::class,
        LocalModule::class,
        RemoteModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<SudokuApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}