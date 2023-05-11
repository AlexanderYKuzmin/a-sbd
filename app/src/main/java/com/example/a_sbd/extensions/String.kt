package com.example.a_sbd.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.parseFromDateDb(): Date {
    val sdf= SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    return sdf.parse(this) ?: throw RuntimeException("Invalid Date format.")
}