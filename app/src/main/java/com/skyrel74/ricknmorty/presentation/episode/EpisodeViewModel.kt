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

    fun getRemoteCount() = episodeRepository.remoteCount

    fun getEpisodes(queryMap: Map<String, String>): Observable<List<Episode>> {
        return episodeRepository.getAll(pageNumber, queryMap)
    }

    fun refresh(queryMap: Map<String, String>): Observable<List<Episode>> =
        episodeRepository.refresh(queryMap)
}