package com.skyrel74.ricknmorty.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.entities.CharacterResult
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.data.entities.EpisodeResult
import java.io.ByteArrayOutputStream

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Drawable.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun CharacterResult.toCharacter(): Character =
    Character(
        this.id,
        this.name,
        this.status,
        this.species,
        this.gender,
        this.image.toUri()
    )

fun EpisodeResult.toEpisode(): Episode =
    Episode(
        this.id,
        this.name,
        this.episode,
        this.airDate
    )