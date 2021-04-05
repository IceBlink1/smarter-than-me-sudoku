package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.ui.MainActivity
import com.smarterthanmesudokuapp.ui.fragments.home.HomeModule
import com.smarterthanmesudokuapp.ui.fragments.login.LoginModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(
        modules = [
            HomeModule::class,
            LoginModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity

}