package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.vironit.learning_android_custom_calendar_kulakov.R
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ItemDayBinding
import java.util.*

class CustomCalendarAdapter(context: Context) : CalendarPagerAdapter(context) {

    override fun onCreateView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
    }

    override fun onBindView(view: View, day: Day) {
        val binding = ItemDayBinding.bind(view)
        val colorRes = if (day.state == DayState.ThisMonth) R.color.black else R.color.grey
        binding.textView.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
        binding.textView.text = day.calendar.get(Calendar.DAY_OF_MONTH).toString()
        binding.eventStar.isVisible = day.isEvent
    }
}