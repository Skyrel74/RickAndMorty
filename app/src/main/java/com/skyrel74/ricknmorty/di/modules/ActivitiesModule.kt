package com.skyrel74.ricknmorty.di.modules

import com.skyrel74.ricknmorty.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}