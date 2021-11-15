package com.skyrel74.ricknmorty.data.repository

import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.data.local.LocationDao
import com.skyrel74.ricknmorty.data.remote.LocationService
import com.skyrel74.ricknmorty.di.Application.Companion.Variables.isNetworkConnected
import com.skyrel74.ricknmorty.util.logError
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val local: LocationDao,
    private val remote: LocationService,
) {

    init {
        local.getCount().subscribe({
            localCount = it
        }, { logError("LocationRepository", it) })
    }

    private var localCount: Int = Int.MAX_VALUE
    private var remoteCount: Int = Int.MAX_VALUE

    fun getAll(page: Int): Observable<List<Location>> =
        if (isNetworkConnected && remoteCount > localCount)
            getRemote(page)
        else
            getLocal()

    fun get(id: Int): Observable<Location> =
        if (isNetworkConnected)
            getRemoteById(id)
        else
            getLocal(id)

    fun refresh(): Observable<List<Location>> = getRemote(1)

    private fun getRemote(page: Int): Observable<List<Location>> =
        remote.getAll(page)
            .map { response ->
                remoteCount = response.info.count
                response.results
            }
            .doOnSuccess { locations ->
                saveLocal(locations)
            }
            .doOnError { logError("LocationRepository", it) }
            .toObservable()

    private fun getRemoteById(id: Int): Observable<Location> =
        remote.get(id)
            .doOnSuccess { locations ->
                saveLocal(locations)
            }
            .doOnError { logError("LocationRepository", it) }
            .toObservable()

    private fun saveLocal(locations: List<Location>) =
        local.insertAll(locations).subscribe({}, { logError("LocationRepository", it) })

    private fun saveLocal(location: Location) =
        local.insert(location).subscribe({}, { logError("LocationRepository", it) })

    private fun getLocal(): Observable<List<Location>> =
        local.getAll().doOnError { logError("LocationRepository", it) }

    private fun getLocal(id: Int): Observable<Location> =
        local.get(id).doOnError { logError("LocationRepository", it) }
}