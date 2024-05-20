package com.example.kotlin_project.util

import android.app.PendingIntent

object GlobalProperties {

    const val TIME_FORMAT = "%02d:%02d:%02d"

    const val pendingIntentFlags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
}
