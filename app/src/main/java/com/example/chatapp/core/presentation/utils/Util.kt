package com.example.chatapp.core.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat

import java.util.Date
import java.util.Locale

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun String.toReadableTime(): String {
    return try {
        if (this.toLongOrNull() != null) {
            // millis formatı (öz göndərdiklərin üçün)
            this.toLong().toTimeString()
        } else {
            // ISO-8601 formatı (serverdən gələnlər üçün)
            val instant = Instant.parse(this)
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.systemDefault())
            formatter.format(instant)
        }
    } catch (e: Exception) {
        "--:--"
    }
}

fun Long.toTimeString(): String {
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(Date(this))
}
