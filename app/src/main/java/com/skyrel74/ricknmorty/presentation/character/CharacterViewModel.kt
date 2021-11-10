package com.skyrel74.ricknmorty.presentation.character

import androidx.lifecycle.ViewModel
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.repository.CharacterRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    var pageNumber: Int = 1

    fun getCharacters(): Observable<List<Character>> = characterRepository.getAll(pageNumber)

    fun refresh(): Observable<List<Character>> = characterRepository.refresh()
}