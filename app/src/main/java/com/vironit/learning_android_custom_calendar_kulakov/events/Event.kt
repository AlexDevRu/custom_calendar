package com.vironit.learning_android_custom_calendar_kulakov.events

import java.util.*

data class Event(
    val id: Long,
    val calendar: Calendar,
    val title: String,
    val color: Int
)
