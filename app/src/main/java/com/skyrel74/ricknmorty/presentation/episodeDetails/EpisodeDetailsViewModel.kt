package com.skyrel74.ricknmorty.presentation.episodeDetails

import androidx.lifecycle.ViewModel
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.repository.CharacterRepository
import com.skyrel74.ricknmorty.data.repository.EpisodeRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EpisodeDetailsViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    var pageNumber: Int = 1

    fun getEpisodeById(id: Int) = episodeRepository.get(id)

    fun getMultipleCharacters(urlList: List<String>): Observable<List<Character>> =
        characterRepository.getMultiple(urlList, pageNumber)
}