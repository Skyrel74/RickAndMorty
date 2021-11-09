package com.skyrel74.ricknmorty.di

import com.skyrel74.ricknmorty.di.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class Application : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder().bindContext(this).build()

    companion object {

        const val API_BASE_URL = "https://rickandmortyapi.com/api/"
    }
}