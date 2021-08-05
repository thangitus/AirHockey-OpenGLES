package com.airhockey.android.util

import android.util.Log

object Logger {
    const val ON = true

    fun v(tag: String, msg: String) =
        log {
            Log.v(tag, msg)
        }

    fun w(tag: String, msg: String) =
        log {
            Log.w(tag, msg)
        }

    private inline fun log(lambda: () -> Unit) {
        if (!ON) return
        lambda.invoke()
    }
}