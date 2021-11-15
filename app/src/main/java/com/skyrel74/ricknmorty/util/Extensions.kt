package com.skyrel74.ricknmorty.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Drawable.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}