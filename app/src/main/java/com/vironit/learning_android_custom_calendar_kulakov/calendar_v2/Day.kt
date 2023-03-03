package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import java.util.*

data class Day(
    val calendar: Calendar,
    val state: DayState,
    val isToday: Boolean,
    val isSelected: Boolean,
    val event: Event? = null
)