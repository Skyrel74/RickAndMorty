package com.skyrel74.ricknmorty.di.modules

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.skyrel74.ricknmorty.data.local.ApplicationDatabase
import com.skyrel74.ricknmorty.data.local.CharacterDao
import com.skyrel74.ricknmorty.data.local.EpisodeDao
import com.skyrel74.ricknmorty.data.local.LocationDao
import com.skyrel74.ricknmorty.util.Converters
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideApplicationDatabase(context: Context, converters: Converters): ApplicationDatabase =
        ApplicationDatabase.getDatabase(context, converters)

    @Singleton
    @Provides
    fun provideGlideRequestManager(context: Context): RequestManager = Glide.with(context)

    @Singleton
    @Provides
    fun provideConverters(
        glideRequestManager: RequestManager,
        context: Context,
    ) = Converters(glideRequestManager, context)

    @Singleton
    @Provides
    fun provideCharacterDao(applicationDatabase: ApplicationDatabase): CharacterDao =
        applicationDatabase.characterDao()

    @Singleton
    @Provides
    fun provideEpisodeDao(applicationDatabase: ApplicationDatabase): EpisodeDao =
        applicationDatabase.episodeDao()

    @Singleton
    @Provides
    fun provideLocationDao(applicationDatabase: ApplicationDatabase): LocationDao =
        applicationDatabase.locationDao()
}