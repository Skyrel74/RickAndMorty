package com.skyrel74.ricknmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyrel74.ricknmorty.data.entities.Episode
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface EpisodeDao {

    @Query("SELECT * FROM episode_table")
    fun getAll(): Observable<List<Episode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(episodeList: List<Episode>): Completable

    @Query("SELECT COUNT(id) FROM episode_table")
    fun getCount(): Int
}