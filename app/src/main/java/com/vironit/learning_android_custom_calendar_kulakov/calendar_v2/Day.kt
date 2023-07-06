package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import java.util.*

enum class Selection {
    START, MIDDLE, END
}

data class Day(
    val calendar: Calendar,
    val state: DayState,
    val isToday: Boolean,
    val isSelected: Boolean,
    val events: List<Event> = emptyList(),
    val selectionType: Selection? = null
)