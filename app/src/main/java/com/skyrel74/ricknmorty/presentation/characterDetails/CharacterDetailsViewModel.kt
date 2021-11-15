package com.skyrel74.ricknmorty.presentation.characterDetails

import androidx.lifecycle.ViewModel
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.repository.CharacterRepository
import com.skyrel74.ricknmorty.data.repository.EpisodeRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CharacterDetailsViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {

    var pageNumber: Int = 1

    fun getCharacterById(id: Int) = characterRepository.get(id)

    fun getMultipleEpisodes(urlList: List<String>): Observable<List<Episode>> =
        episodeRepository.getMultiple(urlList, pageNumber)
}