package com.skyrel74.ricknmorty.data.repository

import android.util.Log
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.local.CharacterDao
import com.skyrel74.ricknmorty.data.remote.CharacterService
import com.skyrel74.ricknmorty.di.Application.Companion.Variables.isNetworkConnected
import com.skyrel74.ricknmorty.util.toCharacter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val local: CharacterDao,
    private val remote: CharacterService,
) {

    init {
        local.getCount().subscribe({
            localCount = it
        }, { logError(it) })
    }

    private var localCount: Int = Int.MAX_VALUE
    private var remoteCount: Int = Int.MAX_VALUE

    fun getAll(page: Int): Observable<List<Character>> =
        if (isNetworkConnected && remoteCount >= localCount)
            getRemote(page)
        else
            getLocal()

    fun refresh(): Observable<List<Character>> = getRemote(1)

    private fun getRemote(page: Int): Observable<List<Character>> =
        remote.getAllCharacters(page)
            .map { response ->
                remoteCount = response.info.count
                response.results.map { it.toCharacter() }
            }
            .doOnSuccess { characters ->
                saveLocal(characters)
            }
            .doOnError { logError(it) }
            .toObservable()

    private fun saveLocal(characters: List<Character>) {
        local.insertAll(characters).subscribe({}, { logError(it) })
    }

    private fun getLocal(): Observable<List<Character>> = local.getAll().doOnError { logError(it) }

    private fun logError(e: Throwable) = Log.e("CharacterRepository", e.stackTraceToString())
}
