package com.skyrel74.ricknmorty.data.repository

import android.util.Log
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.data.local.LocationDao
import com.skyrel74.ricknmorty.data.remote.LocationService
import com.skyrel74.ricknmorty.util.toLocation
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val isNetworkAvailable: Boolean,
    private val local: LocationDao,
    private val remote: LocationService,
) {

    fun getAll(page: Int): Observable<List<Location>> {
        return if (isNetworkAvailable) {
            remote.getAllLocations(page)
                .map { response ->
                    response.results.map { it.toLocation() }
                }
                .doOnSuccess { characters ->
                    local.insertAll(characters).subscribe({}, {
                        Log.e("CharacterRepository", it.stackTraceToString())
                    })
                }.doOnError {
                    Log.e("CharacterRepository", it.stackTraceToString())
                }.toObservable()
        } else {
            local.getAll().doOnError {
                Log.e("CharacterRepository", it.stackTraceToString())
            }
        }
    }
}