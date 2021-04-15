package com.smarterthanmesudokuapp.ui.fragments.auth.password

import com.smarterthanmesudokuapp.di.ViewModelBuilder
import com.smarterthanmesudokuapp.ui.fragments.auth.password.code.RecoverPasswordCodeFragment
import com.smarterthanmesudokuapp.ui.fragments.auth.password.email.RecoverPasswordEmailFragment
import com.smarterthanmesudokuapp.ui.fragments.auth.password.newpassword.RecoverPasswordNewPasswordFragment
import com.smarterthanmesudokuapp.ui.fragments.auth.register.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RecoverPasswordModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun providesRecoverPasswordCodeFragment(): RecoverPasswordCodeFragment

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun providesRecoverPasswordEmailFragment(): RecoverPasswordEmailFragment

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun providesRecoverPasswordNewPasswordFragment(): RecoverPasswordNewPasswordFragment
}