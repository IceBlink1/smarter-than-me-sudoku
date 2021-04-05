package com.smarterthanmesudokuapp.ui.fragments.sudoku

import androidx.lifecycle.ViewModel
import com.smarterthanmesudokuapp.di.ViewModelBuilder
import com.smarterthanmesudokuapp.di.ViewModelKey
import com.smarterthanmesudokuapp.domain.mappers.SudokuMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SudokuModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun sudokuFragment(): SudokuFragment

    @Binds
    @IntoMap
    @ViewModelKey(SudokuViewModel::class)
    abstract fun bindSudokuViewModel(viewmodel: SudokuViewModel): ViewModel
}