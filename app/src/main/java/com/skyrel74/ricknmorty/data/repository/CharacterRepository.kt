package com.skyrel74.ricknmorty.data.repository

import android.util.Log
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.local.CharacterDao
import com.skyrel74.ricknmorty.data.remote.CharacterService
import com.skyrel74.ricknmorty.util.toCharacter
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val isNetworkAvailable: Boolean,
    private val local: CharacterDao,
    private val remote: CharacterService,
) {

    fun getAll(page: Int): Observable<List<Character>> {
        return if (isNetworkAvailable) {
            remote.getAllCharacters(page)
                .map { response ->
                    response.results.map { it.toCharacter() }
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