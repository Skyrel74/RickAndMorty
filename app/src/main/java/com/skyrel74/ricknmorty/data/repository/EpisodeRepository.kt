package com.skyrel74.ricknmorty.data.repository

import android.util.Log
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.local.EpisodeDao
import com.skyrel74.ricknmorty.data.remote.EpisodeService
import com.skyrel74.ricknmorty.util.toEpisode
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val local: EpisodeDao,
    private val remote: EpisodeService,
) {

    fun getAll(page: Int): Observable<List<Episode>> {
        if (isNetworkAvailable) {
            remote.getAllEpisodes(page)
                .map { response ->
                    response.results.map { it.toEpisode() }
                }
                .doOnSuccess { characters ->
                    local.insertAll(characters).subscribe({}, {
                        Log.e("EpisodeRepository", it.stackTraceToString())
                    })
                }.doOnError {
                    Log.e("EpisodeRepository", it.stackTraceToString())
                }.toObservable()
        }
        return local.getAll().doOnError {
            Log.e("EpisodeRepository", it.stackTraceToString())
        }
    }
}