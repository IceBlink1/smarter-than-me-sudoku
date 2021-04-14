package com.smarterthanmesudokuapp.ui.fragments.camera

import com.smarterthanmesudokuapp.di.ViewModelBuilder
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CameraModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    abstract fun cameraFragment(): CameraFragment

}