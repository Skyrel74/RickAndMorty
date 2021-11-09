package com.skyrel74.ricknmorty.di.modules

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.skyrel74.ricknmorty.data.local.CharacterDao
import com.skyrel74.ricknmorty.data.local.EpisodeDao
import com.skyrel74.ricknmorty.data.local.LocationDao
import com.skyrel74.ricknmorty.data.remote.CharacterService
import com.skyrel74.ricknmorty.data.remote.EpisodeService
import com.skyrel74.ricknmorty.data.remote.LocationService
import com.skyrel74.ricknmorty.data.repository.CharacterRepository
import com.skyrel74.ricknmorty.data.repository.EpisodeRepository
import com.skyrel74.ricknmorty.data.repository.LocationRepository
import com.skyrel74.ricknmorty.di.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: Application) = application.applicationContext

    @Provides
    @Singleton
    fun provideIsNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
                return true
        }

        return false
    }

    @Provides
    @Singleton
    fun provideCharactersRepository(
        isNetworkAvailable: Boolean,
        local: CharacterDao,
        remote: CharacterService,
    ): CharacterRepository = CharacterRepository(isNetworkAvailable, local, remote)

    @Provides
    @Singleton
    fun provideEpisodeRepository(
        isNetworkAvailable: Boolean,
        local: EpisodeDao,
        remote: EpisodeService,
    ): EpisodeRepository = EpisodeRepository(isNetworkAvailable, local, remote)

    @Provides
    @Singleton
    fun provideLocationRepository(
        isNetworkAvailable: Boolean,
        local: LocationDao,
        remote: LocationService,
    ): LocationRepository = LocationRepository(isNetworkAvailable, local, remote)
}