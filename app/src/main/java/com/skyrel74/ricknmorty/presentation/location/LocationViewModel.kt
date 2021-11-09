package com.skyrel74.ricknmorty.presentation.location

import androidx.lifecycle.ViewModel
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.data.repository.LocationRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
) : ViewModel() {

    var pageNumber: Int = 1

    fun getLocations(): Observable<List<Location>> = locationRepository.getAll(pageNumber)
}