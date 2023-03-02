package com.vironit.learning_android_custom_calendar_kulakov

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v2.CustomCalendarAdapter
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ActivityCalendarV2Binding
import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import com.vironit.learning_android_custom_calendar_kulakov.events.EventAdapter
import com.vironit.learning_android_custom_calendar_kulakov.events.EventDialog
import org.apache.commons.lang3.time.DateUtils
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class CalendarV2Activity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, SharedPreferences.OnSharedPreferenceChangeListener, EventAdapter.Listener {

    private lateinit var binding: ActivityCalendarV2Binding

    private var flag = 0

    private val prefs by lazy {
        getSharedPreferences("CalendarPrefs", MODE_PRIVATE)
    }

    private val adapter = CustomCalendarAdapter(this)

    private val eventAdapter = EventAdapter(this)

    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarV2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val shortWeekdays = DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays.drop(1)
        val weekLettersTextViews = listOf(
            binding.tvDayOfWeek1,
            binding.tvDayOfWeek2,
            binding.tvDayOfWeek3,
            binding.tvDayOfWeek4,
            binding.tvDayOfWeek5,
            binding.tvDayOfWeek6,
            binding.tvDayOfWeek7
        )

        shortWeekdays.zip(weekLettersTextViews).forEach { (date, textView) ->
            textView.root.text = date.first().toString()
        }

        binding.btnMonthBack.setOnClickListener {
            binding.viewPager.moveItemBy(-1, true)
        }
        binding.btnMonthNext.setOnClickListener {
            binding.viewPager.moveItemBy(1, true)
        }

        binding.viewPager.adapter = adapter

        binding.viewPager.onCalendarChangeListener = {
            updateMonthTitle(it)
        }

        binding.calendarTitle.setOnClickListener {
            flag = 0
            val calendar = binding.viewPager.getCurrentCalendar()!!
            DatePickerDialog(
                this,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        supportFragmentManager.setFragmentResultListener("EventDialog", this) { _, bundle ->
            val wedding = bundle.getBoolean(EventDialog.WEDDING)
            val birthday = bundle.getBoolean(EventDialog.BIRTHDAY)
            val graduation = bundle.getBoolean(EventDialog.GRADUATION)
            val date = bundle.getString(EventDialog.DATE)
            val filteredDates = getDates().filter {
                val parts = it.split(" ")
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val dayOfMonth = parts[2].toInt()
                "$year $month $dayOfMonth" != date
            }.toSet()
            prefs.edit { putStringSet(DATES, filteredDates + "$date $wedding $birthday $graduation") }
        }

        binding.btnAddEvent.setOnClickListener {
            flag = 1
            EventDialog().show(supportFragmentManager, null)
        }

        prefs.registerOnSharedPreferenceChangeListener(this)

        binding.rvEvents.adapter = eventAdapter
        updateDates()

        updateMonthTitle(Calendar.getInstance())
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        if (flag == 0) {
            val calendar = binding.viewPager.getCurrentCalendar()!!
            val diff = month - calendar.get(Calendar.MONTH)
            binding.viewPager.moveItemBy(diff, true)
        } else if (flag == 1) {
            prefs.edit { putStringSet(DATES, getDates() + "$year $month $dayOfMonth") }
        }
    }

    private fun getDates() = prefs.getStringSet(DATES, null).orEmpty()

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, key: String?) {
        when (key) {
            DATES -> updateDates()
        }
    }

    private fun updateDates() {
        val dates = getDates().map {
            val parts = it.split(" ")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val dayOfMonth = parts[2].toInt()
            val wedding = parts[3].toBoolean()
            val birthday = parts[4].toBoolean()
            val graduation = parts[5].toBoolean()
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            Event(
                calendar = cal,
                wedding = wedding,
                birthday = birthday,
                graduation = graduation
            )
        }.sortedBy { it.calendar.time }
        adapter.events = dates
        eventAdapter.submitList(dates)
    }

    override fun onRemove(event: Event) {
        val calendar = event.calendar

        val newDates = getDates().filter {
            val parts = it.split(" ")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val dayOfMonth = parts[2].toInt()
            val calendar1 = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }
            !DateUtils.isSameDay(calendar, calendar1)
        }.toSet()

        prefs.edit { putStringSet(DATES, newDates) }
    }

    private fun updateMonthTitle(calendar: Calendar) {
        binding.calendarTitle.text = sdf.format(calendar.time)
    }

    companion object {
        private const val DATES = "dates"
    }

}