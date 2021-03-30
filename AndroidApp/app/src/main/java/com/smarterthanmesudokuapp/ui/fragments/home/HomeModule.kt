package com.smarterthanmesudokuapp.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.smarterthanmesudokuapp.di.ViewModelBuilder
import com.smarterthanmesudokuapp.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun addHomeFragment(): HomeFragment


    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindViewModel(viewModel: HomeViewModel): ViewModel
}