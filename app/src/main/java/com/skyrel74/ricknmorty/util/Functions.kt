package com.skyrel74.ricknmorty.util

import android.util.Log

fun logError(tag: String, e: Throwable) = Log.e(tag, e.stackTraceToString())