package com.skyrel74.ricknmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.util.Converters

@Database(entities = [
    Character::class,
    Episode::class,
    Location::class
], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun locationDao(): LocationDao

    companion object {

        @Volatile
        private var instance: ApplicationDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context, converters: Converters): ApplicationDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context, converters).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context, converters: Converters): ApplicationDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context,
                    ApplicationDatabase::class.java,
                    "application_db"
                )
                    .addTypeConverter(converters)
                    .fallbackToDestructiveMigration()
                    .build()
            return instance as ApplicationDatabase
        }
    }
}