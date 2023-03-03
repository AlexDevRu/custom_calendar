package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
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
        binding.textView.text = day.calendar.get(Calendar.DAY_OF_MONTH).toString()

        val textColorRes = if (day.state == DayState.ThisMonth)
            if (day.event != null)
                R.color.white
            else
                R.color.black
        else
            R.color.grey
        binding.textView.setTextColor(ContextCompat.getColor(view.context, textColorRes))

        binding.bgSelectionStart.isVisible = day.selectionType == Selection.START
        binding.bgSelectionEnd.isVisible = day.selectionType == Selection.END
        if (day.selectionType == Selection.MIDDLE)
            binding.root.setBackgroundColor(ContextCompat.getColor(view.context, R.color.teal_200))
        else
            binding.root.background = null

        if (day.state == DayState.ThisMonth && day.event != null) {
            val colors = mutableListOf<Int>()

            if (day.event.wedding)
                colors.add(ContextCompat.getColor(view.context, R.color.wedding))
            if (day.event.birthday)
                colors.add(ContextCompat.getColor(view.context, R.color.birthday))
            if (day.event.graduation)
                colors.add(ContextCompat.getColor(view.context, R.color.graduation))

            if (colors.size > 1) {
                val gd = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    colors.toIntArray()
                )
                gd.shape = GradientDrawable.OVAL
                binding.textView.background = gd
            } else if (colors.size == 1) {
                val bg = ShapeDrawable(OvalShape())
                bg.paint.color = colors.first()
                binding.textView.background = bg
            } else {
                binding.textView.background = null
            }

        } else {
            binding.textView.background = null
        }
    }
}