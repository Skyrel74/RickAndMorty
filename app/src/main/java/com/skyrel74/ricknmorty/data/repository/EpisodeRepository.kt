package com.skyrel74.ricknmorty.data.repository

import android.util.Log
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.local.EpisodeDao
import com.skyrel74.ricknmorty.data.remote.EpisodeService
import com.skyrel74.ricknmorty.di.Application.Companion.Variables.isNetworkConnected
import com.skyrel74.ricknmorty.util.toEpisode
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val local: EpisodeDao,
    private val remote: EpisodeService,
) {

    fun getAll(page: Int): Observable<List<Episode>> =
        if (isNetworkConnected)
            getRemote(page)
        else
            getLocal()

    fun refresh(): Observable<List<Episode>> = getRemote(1)

    private fun getRemote(page: Int): Observable<List<Episode>> =
        remote.getAllEpisodes(page)
            .map { response ->
                response.results.map { it.toEpisode() }
            }
            .doOnSuccess { episodes ->
                saveLocal(episodes)
            }
            .doOnError { logError(it) }
            .toObservable()

    private fun saveLocal(episodes: List<Episode>) {
        local.insertAll(episodes).subscribe({}, { logError(it) })
    }

    private fun getLocal(): Observable<List<Episode>> = local.getAll().doOnError { logError(it) }

    private fun logError(e: Throwable) = Log.e("EpisodeRepository", e.stackTraceToString())
}