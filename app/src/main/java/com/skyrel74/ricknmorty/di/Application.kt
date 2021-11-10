package com.skyrel74.ricknmorty.di

import android.util.Log
import com.skyrel74.ricknmorty.di.components.DaggerApplicationComponent
import com.skyrel74.ricknmorty.util.NetworkMonitor
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kotlin.properties.Delegates

class Application : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder().bindContext(this).build()

    override fun onCreate() {
        super.onCreate()
        NetworkMonitor(this).startNetworkCallback()
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }

    companion object {

        const val API_BASE_URL = "https://rickandmortyapi.com/api/"

        object Variables {
            var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
                Log.i("Network connectivity", "$newValue")
            }
        }
    }
}