package com.skyrel74.ricknmorty.data.repository

import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.local.CharacterDao
import com.skyrel74.ricknmorty.data.remote.CharacterService
import com.skyrel74.ricknmorty.di.Application.Companion.Variables.isNetworkConnected
import com.skyrel74.ricknmorty.presentation.character.CharacterAdapter.Companion.VISIBLE_THRESHOLD
import com.skyrel74.ricknmorty.util.logError
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val local: CharacterDao,
    private val remote: CharacterService,
) {

    init {
        local.getCount().subscribe({
            localCount = it
        }, { logError("CharacterRepository", it) })
    }

    private var localCount: Int = Int.MAX_VALUE
    private var remoteCount: Int = Int.MAX_VALUE
    private var localMultipleCount: Int = 0

    fun getAll(page: Int): Observable<List<Character>> =
        if (isNetworkConnected && remoteCount > localCount)
            getRemote(page)
        else
            getLocal()

    fun get(id: Int): Observable<Character> =
        if (isNetworkConnected)
            getRemoteById(id)
        else
            getLocal(id)

    fun getMultiple(urlList: List<String>, page: Int): Observable<List<Character>> {
        val idList: List<Int> = urlList.map {
            val startIndex = it.lastIndexOf("/") + 1
            it.substring(startIndex, it.length).toInt()
        }

        local.getMultipleCount(idList).subscribe({
            localMultipleCount = it
        }, { logError("CharacterRepository", it) })

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

    fun refresh(): Observable<List<Character>> = getRemote(1)

    private fun getRemote(page: Int): Observable<List<Character>> =
        remote.getAll(page)
            .map { response ->
                remoteCount = response.info.count
                response.results
            }
            .doOnSuccess { characters ->
                saveLocal(characters)
            }
            .doOnError { logError("CharacterRepository", it) }
            .toObservable()

    private fun getRemoteById(id: Int): Observable<Character> =
        remote.get(id)
            .doOnSuccess { character ->
                saveLocal(character)
            }
            .doOnError { logError("CharacterRepository", it) }
            .toObservable()

    private fun getRemoteMultiple(idList: List<Int>): Observable<List<Character>> {
        return remote.getMultiple(idList.joinToString(separator = ","))
            .doOnSuccess { characters ->
                saveLocal(characters)
            }
            .doOnError { logError("CharacterRepository", it) }
            .toObservable()
    }

    private fun saveLocal(characters: List<Character>) =
        local.insertAll(characters).subscribe({}, { logError("CharacterRepository", it) })

    private fun saveLocal(character: Character) =
        local.insert(character).subscribe({}, { logError("CharacterRepository", it) })

    private fun getLocal(): Observable<List<Character>> =
        local.getAll().doOnError { logError("CharacterRepository", it) }

    private fun getLocal(id: Int): Observable<Character> =
        local.get(id).doOnError { logError("CharacterRepository", it) }

    private fun getLocal(idList: List<Int>): Observable<List<Character>> =
        local.getMultiple(idList).doOnError { logError("EpisodeRepository", it) }
}
