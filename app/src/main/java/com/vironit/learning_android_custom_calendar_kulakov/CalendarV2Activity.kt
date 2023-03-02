package com.vironit.learning_android_custom_calendar_kulakov

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v2.CustomCalendarAdapter
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ActivityCalendarV2Binding
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class CalendarV2Activity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityCalendarV2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarV2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val shortWeekdays = DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays.drop(1)
        val weekLettersTextViews = binding.llWeek.children.toList()

        shortWeekdays.zip(weekLettersTextViews).forEach { (date, textView) ->
            (textView as? TextView)?.text = date.first().toString()
        }

        binding.btnMonthBack.setOnClickListener {
            binding.viewPager.moveItemBy(-1, true)
        }
        binding.btnMonthNext.setOnClickListener {
            binding.viewPager.moveItemBy(1, true)
        }

        binding.viewPager.adapter = CustomCalendarAdapter(this)

        binding.viewPager.onCalendarChangeListener = {
            updateMonthTitle(it)
        }

        binding.calendarTitle.setOnClickListener {
            val calendar = binding.viewPager.getCurrentCalendar()!!
            DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        updateMonthTitle(Calendar.getInstance())
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, month: Int, p3: Int) {
        val calendar = binding.viewPager.getCurrentCalendar()!!
        val diff = month - calendar.get(Calendar.MONTH)
        binding.viewPager.moveItemBy(diff, true)
    }

    private fun updateMonthTitle(calendar: Calendar) {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.calendarTitle.text = sdf.format(calendar.time)
    }

}