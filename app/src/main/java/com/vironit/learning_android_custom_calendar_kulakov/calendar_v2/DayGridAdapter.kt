package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.lang3.time.DateUtils
import java.util.*
import kotlin.properties.Delegates

abstract class CalendarCellAdapter(
    private val calendar: Calendar,
    startingAt: CalendarPagerAdapter.DayOfWeek,
    preselectedDay: Date? = null,
    preselectedEvents: List<Date> = emptyList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val weekOfMonth: Int
    private val startDate: Calendar

    var items: List<Day> by Delegates.observable(emptyList()) { _, old, new ->
        CalendarDiff(old, new).calculateDiff().dispatchUpdatesTo(this)
    }

    init {
        val start = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH)
        if (start.get(Calendar.DAY_OF_WEEK) != (startingAt.getDifference() + 1)) {
            start.set(
                Calendar.DAY_OF_MONTH,
                if (startingAt.isLessFirstWeek(calendar)) -startingAt.getDifference() else 0
            )
            start.add(
                Calendar.DAY_OF_MONTH,
                -start.get(Calendar.DAY_OF_WEEK) + 1 + startingAt.getDifference()
            )
        }
        startDate = start
        this.weekOfMonth =
            calendar.getActualMaximum(Calendar.WEEK_OF_MONTH) + (if (startingAt.isLessFirstWeek(
                    calendar
                )
            ) 1 else 0) - (if (startingAt.isMoreLastWeek(calendar)) 1 else 0)
        updateItems(preselectedDay, preselectedEvents)
    }

    fun updateItems(selectedDate: Date? = null, events: List<Date> = emptyList()) {
        val now = Calendar.getInstance()

        this.items = (0..itemCount).map {
            val cal = Calendar.getInstance().apply { time = startDate.time }
            cal.add(Calendar.DAY_OF_MONTH, it)

            val thisTime = calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH)
            val compareTime = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH)

            val state = when (thisTime.compareTo(compareTime)) {
                -1 -> DayState.NextMonth
                0 -> DayState.ThisMonth
                1 -> DayState.PreviousMonth
                else -> throw IllegalStateException()
            }
            val isSelected = when (selectedDate) {
                null -> false
                else -> DateUtils.isSameDay(cal.time, selectedDate)
            }
            val isToday = DateUtils.isSameDay(cal, now)

            val isEvent = events.any {
                DateUtils.isSameDay(cal.time, it)
            }

            Day(cal, state, isToday, isSelected, isEvent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, items[holder.layoutPosition])
    }

    override fun getItemCount(): Int = 7 * weekOfMonth

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, day: Day)
}

data class Day(
    var calendar: Calendar,
    var state: DayState,
    var isToday: Boolean,
    var isSelected: Boolean,
    var isEvent: Boolean
)

enum class DayState {
    PreviousMonth,
    ThisMonth,
    NextMonth
}
