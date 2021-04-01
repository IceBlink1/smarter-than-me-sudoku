package com.smarterthanmesudokuapp.di

import android.content.Context
import com.smarterthanmesudokuapp.SudokuApplication
import com.smarterthanmesudokuapp.ui.fragments.camera.CameraModule
import com.smarterthanmesudokuapp.ui.fragments.dashboard.DashboardModule
import com.smarterthanmesudokuapp.ui.fragments.home.HomeModule
import com.smarterthanmesudokuapp.ui.fragments.notifications.NotificationsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        HomeModule::class,
        ActivityModule::class,
        ApplicationModule::class,
        LocalModule::class,
        RemoteModule::class,
        RecognitionModule::class,
        DashboardModule::class,
        NotificationsModule::class,
        CameraModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<SudokuApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}