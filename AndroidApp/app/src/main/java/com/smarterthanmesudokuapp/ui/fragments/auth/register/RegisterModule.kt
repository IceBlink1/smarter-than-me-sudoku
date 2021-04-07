package com.smarterthanmesudokuapp.ui.fragments.auth.register

import androidx.lifecycle.ViewModel
import com.smarterthanmesudokuapp.di.ViewModelBuilder
import com.smarterthanmesudokuapp.di.ViewModelKey
import com.smarterthanmesudokuapp.ui.fragments.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RegisterModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun provideRegisterFragment(): RegisterFragment



}