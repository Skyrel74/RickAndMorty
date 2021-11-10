package com.skyrel74.ricknmorty.data.repository

import android.util.Log
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.data.local.LocationDao
import com.skyrel74.ricknmorty.data.remote.LocationService
import com.skyrel74.ricknmorty.di.Application.Companion.Variables.isNetworkConnected
import com.skyrel74.ricknmorty.util.toLocation
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val local: LocationDao,
    private val remote: LocationService,
) {

    init {
        local.getCount().subscribe({
            localCount = it
        }, { logError(it) })
    }

    private var localCount: Int = Int.MAX_VALUE
    private var remoteCount: Int = Int.MAX_VALUE

    fun getAll(page: Int): Observable<List<Location>> =
        if (isNetworkConnected && remoteCount >= localCount)
            getRemote(page)
        else
            getLocal()

    fun refresh(): Observable<List<Location>> = getRemote(1)

    private fun getRemote(page: Int): Observable<List<Location>> =
        remote.getAllLocations(page)
            .map { response ->
                remoteCount = response.info.count
                response.results.map { it.toLocation() }
            }
            .doOnSuccess { locations ->
                saveLocal(locations)
            }
            .doOnError { logError(it) }
            .toObservable()

    private fun saveLocal(locations: List<Location>) {
        local.insertAll(locations).subscribe({}, { logError(it) })
    }

    private fun getLocal(): Observable<List<Location>> = local.getAll().doOnError { logError(it) }

    private fun logError(e: Throwable) = Log.e("LocationRepository", e.stackTraceToString())
}