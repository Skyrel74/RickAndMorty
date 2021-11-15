package com.skyrel74.ricknmorty.util

import android.content.Context
import android.net.Uri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.skyrel74.ricknmorty.data.entities.CharacterLocation
import java.io.File
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val glideRequestManager: RequestManager,
    private val context: Context,
) {

    @TypeConverter
    fun stringListFromString(stringListString: String): List<String> =
        stringListString.split(",").map { it }

    @TypeConverter
    fun stringListToString(stringList: List<String>): String =
        stringList.joinToString(separator = ",")

    @TypeConverter
    fun characterLocationFromString(stringLocation: String): CharacterLocation {
        val fields: List<String> = stringLocation.split(",")
        return CharacterLocation(
            fields[0],
            fields[1]
        )
    }

    @TypeConverter
    fun characterLocationToString(location: CharacterLocation): String =
        "${location.name},${location.url}"

    @TypeConverter
    fun uriFromString(string: String): Uri = Uri.parse(string)

    @TypeConverter
    fun uriToString(url: Uri): String = saveToFile(url).toString()

    private fun saveToFile(url: Uri): Uri {
        val drawable = glideRequestManager
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .centerCrop()
            .submit()
            .get()
        val filename = url.toString().substringAfterLast("/")
        val file = File(context.filesDir, filename)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { stream ->
            stream?.write(drawable.toByteArray())
        }
        return Uri.fromFile(file)
    }
}