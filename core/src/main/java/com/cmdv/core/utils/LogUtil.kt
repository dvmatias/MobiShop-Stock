package com.cmdv.core.utils

import android.util.Log
import com.cmdv.core.Constants.Companion.TAG_LOGGER


fun logErrorMessage(message: String) {
    Log.e(TAG_LOGGER, message)
}

fun logInfoMessage(message: String) {
    Log.i(TAG_LOGGER, message)
}

fun logWarningMessage(message: String) {
    Log.w(TAG_LOGGER, message)
}

fun logDebugMessage(message: String) {
    Log.d(TAG_LOGGER, message)
}