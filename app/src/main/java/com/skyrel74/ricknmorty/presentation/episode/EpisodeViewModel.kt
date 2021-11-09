package com.skyrel74.ricknmorty.presentation.episode

import androidx.lifecycle.ViewModel
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.repository.EpisodeRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EpisodeViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    var pageNumber: Int = 1

    fun getEpisodes(): Observable<List<Episode>> = episodeRepository.getAll(pageNumber)
}