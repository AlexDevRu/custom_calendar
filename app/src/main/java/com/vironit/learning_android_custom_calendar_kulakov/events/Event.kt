package com.vironit.learning_android_custom_calendar_kulakov.events

import java.util.*

data class Event(
    val calendar: Calendar,
    val wedding: Boolean,
    val birthday: Boolean,
    val graduation: Boolean,
)
