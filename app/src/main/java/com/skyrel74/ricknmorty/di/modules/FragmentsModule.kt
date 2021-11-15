package com.skyrel74.ricknmorty.di.modules

import com.skyrel74.ricknmorty.presentation.character.CharacterFragment
import com.skyrel74.ricknmorty.presentation.characterDetails.CharacterDetailsFragment
import com.skyrel74.ricknmorty.presentation.episode.EpisodeFragment
import com.skyrel74.ricknmorty.presentation.location.LocationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun characterFragment(): CharacterFragment

    @ContributesAndroidInjector
    abstract fun locationFragment(): LocationFragment

    @ContributesAndroidInjector
    abstract fun episodeFragment(): EpisodeFragment

    @ContributesAndroidInjector
    abstract fun characterDetailsFragment(): CharacterDetailsFragment
}