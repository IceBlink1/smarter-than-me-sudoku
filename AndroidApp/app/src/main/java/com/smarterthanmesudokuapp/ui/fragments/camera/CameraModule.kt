package com.smarterthanmesudokuapp.ui.fragments.camera

import androidx.lifecycle.ViewModel
import com.smarterthanmesudokuapp.di.ViewModelBuilder
import com.smarterthanmesudokuapp.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CameraModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    abstract fun cameraFragment(): CameraFragment

    @IntoMap
    @Binds
    @ViewModelKey(CameraViewModel::class)
    abstract fun bindViewModel(viewModel: CameraViewModel): ViewModel

}