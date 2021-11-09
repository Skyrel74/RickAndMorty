package com.skyrel74.ricknmorty.di.modules

import com.skyrel74.ricknmorty.presentation.characters.CharactersFragment
import com.skyrel74.ricknmorty.presentation.episodes.EpisodesFragment
import com.skyrel74.ricknmorty.presentation.locations.LocationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun charactersFragment(): CharactersFragment

    @ContributesAndroidInjector
    abstract fun locationsFragment(): LocationsFragment

    @ContributesAndroidInjector
    abstract fun episodesFragment(): EpisodesFragment
}