package com.smarterthanmesudokuapp.di

import androidx.lifecycle.ViewModel
import com.smarterthanmesudokuapp.ui.fragments.home.HomeFragment
import com.smarterthanmesudokuapp.ui.fragments.home.HomeViewModel
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