package com.skyrel74.ricknmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skyrel74.ricknmorty.data.entities.Location
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface LocationDao {

    @Query("SELECT * FROM location_table")
    fun getAll(): Observable<List<Location>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locationList: List<Location>): Completable

    @Query("SELECT COUNT(id) FROM location_table")
    fun getCount(): Observable<Int>
}