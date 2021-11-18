package com.skyrel74.ricknmorty.presentation.locationDetails

import androidx.lifecycle.ViewModel
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.repository.CharacterRepository
import com.skyrel74.ricknmorty.data.repository.LocationRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocationDetailsViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    var pageNumber: Int = 1

    fun getLocationById(id: Int) = locationRepository.get(id)

    fun getMultipleCharacters(urlList: List<String>): Observable<List<Character>> =
        characterRepository.getMultiple(urlList, pageNumber)
}