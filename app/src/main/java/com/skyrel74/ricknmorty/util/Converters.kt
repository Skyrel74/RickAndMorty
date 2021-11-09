package com.skyrel74.ricknmorty.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val glideRequestManager: RequestManager,
    private val context: Context,
) {

    @TypeConverter
    fun fromString(string: String): Uri = Uri.parse(string)

    @TypeConverter
    fun toString(url: Uri): String = saveToFile(url).toString()

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