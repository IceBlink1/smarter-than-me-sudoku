package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.auth.login.LoginModule
import com.smarterthanmesudokuapp.ui.fragments.auth.register.RegisterModule
import com.smarterthanmesudokuapp.ui.fragments.home.HomeModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(
        modules = [
            HomeModule::class,
            LoginModule::class,
            RegisterModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity

}