package com.skyrel74.ricknmorty.data.repository

import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.local.EpisodeDao
import com.skyrel74.ricknmorty.data.remote.EpisodeService
import com.skyrel74.ricknmorty.di.Application.Companion.Variables.isNetworkConnected
import com.skyrel74.ricknmorty.presentation.characterDetails.CharacterDetailsAdapter.Companion.VISIBLE_THRESHOLD
import com.skyrel74.ricknmorty.util.logError
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class EpisodeRepository @Inject constructor(
    private val local: EpisodeDao,
    private val remote: EpisodeService,
) {

    private var localCount: Int = Int.MAX_VALUE
    var remoteCount: Int = Int.MAX_VALUE
    private var localMultipleCount: Int = 0

    fun getAll(page: Int, queryMap: Map<String, String>): Observable<List<Episode>> {

        val query: String = queryMap.map { "${it.key}=${it.value}" }.joinToString(separator = "&")

        local.getCount(query).subscribe({
            localCount = it
        }, { logError("EpisodeRepository", it) })

        return if (isNetworkConnected && remoteCount > localCount)
            getRemote(page, queryMap)
        else
            getLocal(query)
    }

    fun get(id: Int): Observable<Episode> =
        if (isNetworkConnected)
            getRemoteById(id)
        else
            getLocal(id)

    fun getMultiple(urlList: List<String>, page: Int): Observable<List<Episode>> {
        val idList: List<Int> = urlList.map {
            val startIndex = it.lastIndexOf("/") + 1
            it.substring(startIndex, it.length).toInt()
        }

        local.getMultipleCount(idList).subscribe({
            localMultipleCount = it
        }, { logError("EpisodeRepository", it) })

        val startIndex: Int =
            if (idList.size > VISIBLE_THRESHOLD)
                VISIBLE_THRESHOLD * (page - 1)
            else
                0
        val endIndex =
            if (VISIBLE_THRESHOLD * page >= idList.size && idList.size > VISIBLE_THRESHOLD)
                VISIBLE_THRESHOLD * page
            else
                idList.size
        val subList = idList.subList(startIndex, endIndex)
        return if (isNetworkConnected && idList.size > localMultipleCount)
            getRemoteMultiple(subList)
        else
            getLocal(subList)
    }

    fun refresh(queryMap: Map<String, String>): Observable<List<Episode>> = getRemote(1, queryMap)

    private fun getRemote(page: Int, queryMap: Map<String, String>): Observable<List<Episode>> =
        remote.getAll(page, queryMap)
            .map { response ->
                remoteCount = response.info.count
                response.results
            }
            .doOnSuccess { episodes ->
                saveLocal(episodes)
            }
            .doOnError { logError("EpisodeRepository", it) }
            .toObservable()

    private fun getRemoteById(id: Int): Observable<Episode> =
        remote.get(id)
            .doOnSuccess { episode ->
                saveLocal(episode)
            }
            .doOnError { logError("EpisodeRepository", it) }
            .toObservable()

    private fun getRemoteMultiple(idList: List<Int>): Observable<List<Episode>> =
        remote.getMultiple(idList.joinToString(separator = ","))
            .doOnSuccess { episodes ->
                saveLocal(episodes)
            }
            .doOnError { logError("EpisodeRepository", it) }
            .toObservable()

    private fun saveLocal(episodes: List<Episode>) =
        local.insertAll(episodes).subscribe({}, { logError("EpisodeRepository", it) })

    private fun saveLocal(episode: Episode) =
        local.insert(episode).subscribe({}, { logError("EpisodeRepository", it) })

    private fun getLocal(query: String): Observable<List<Episode>> =
        local.getAll(query).doOnError { logError("EpisodeRepository", it) }

    private fun getLocal(id: Int): Observable<Episode> =
        local.get(id).doOnError { logError("EpisodeRepository", it) }

    private fun getLocal(idList: List<Int>): Observable<List<Episode>> =
        local.getMultiple(idList).doOnError { logError("EpisodeRepository", it) }
}