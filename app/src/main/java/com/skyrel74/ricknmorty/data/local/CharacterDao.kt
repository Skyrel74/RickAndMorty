package com.skyrel74.ricknmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyrel74.ricknmorty.data.entities.Character
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character_table")
    fun getAll(): Observable<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characterList: List<Character>): Completable

    @Query("SELECT COUNT(id) FROM character_table")
    fun getCount(): Int
}