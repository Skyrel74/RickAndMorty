package com.skyrel74.ricknmorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.skyrel74.ricknmorty.util.Converters
import com.skyrel74.ricknmorty.data.entities.Character

@Database(entities = [
    Character::class
], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

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