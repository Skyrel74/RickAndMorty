package com.skyrel74.ricknmorty.di.modules

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
    fun provideCharactersRepository(
        local: CharacterDao,
        remote: CharacterService,
    ): CharacterRepository = CharacterRepository(local, remote)

    @Provides
    @Singleton
    fun provideEpisodeRepository(
        local: EpisodeDao,
        remote: EpisodeService,
    ): EpisodeRepository = EpisodeRepository(local, remote)

    @Provides
    @Singleton
    fun provideLocationRepository(
        local: LocationDao,
        remote: LocationService,
    ): LocationRepository = LocationRepository(local, remote)
}