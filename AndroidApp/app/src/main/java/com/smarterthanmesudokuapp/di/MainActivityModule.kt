package com.smarterthanmesudokuapp.di

import com.smarterthanmesudokuapp.ui.fragments.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment
}