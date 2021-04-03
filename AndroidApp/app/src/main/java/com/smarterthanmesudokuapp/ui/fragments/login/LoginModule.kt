package com.smarterthanmesudokuapp.ui.fragments.login

import androidx.lifecycle.ViewModel
import com.smarterthanmesudokuapp.di.ViewModelBuilder
import com.smarterthanmesudokuapp.di.ViewModelKey
import com.smarterthanmesudokuapp.ui.fragments.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class LoginModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun provideLoginFragment(): LoginFragment


    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindViewModel(viewmodel: LoginViewModel): ViewModel

}