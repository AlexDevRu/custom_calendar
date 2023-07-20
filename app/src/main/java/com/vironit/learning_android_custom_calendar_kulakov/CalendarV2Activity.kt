package com.vironit.learning_android_custom_calendar_kulakov

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v2.CalendarViewModel
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v2.ChooseDateDialog
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v2.CustomCalendarAdapter
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ActivityCalendarV2Binding
import com.vironit.learning_android_custom_calendar_kulakov.events.Event
import com.vironit.learning_android_custom_calendar_kulakov.events.EventAdapter
import com.vironit.learning_android_custom_calendar_kulakov.events.EventDialog
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarV2Activity : AppCompatActivity(), EventAdapter.Listener {

    private lateinit var binding: ActivityCalendarV2Binding

    private val adapter = CustomCalendarAdapter(this)

    private val eventAdapter = EventAdapter(this)

    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    private var isSelectRange = false

    private val viewModel by viewModels<CalendarViewModel>()

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

        binding.viewPager.onDayClickListener = {
            if (isSelectRange) {
                if (adapter.selectStartDate == null)
                    adapter.selectStartDate = it
                else
                    adapter.selectEndDate = it
            }
        }

        binding.calendarTitle.setOnClickListener {
            val calendar = binding.viewPager.getCurrentCalendar()!!
            ChooseDateDialog.createInstance(calendar.time.time).show(supportFragmentManager, null)
        }

        supportFragmentManager.setFragmentResultListener(EventDialog.REQUEST_KEY, this) { _, bundle ->
            val color = bundle.getInt(EventDialog.COLOR)
            val title = bundle.getString(EventDialog.TITLE).orEmpty()
            val date = bundle.getLong(EventDialog.DATE)
            viewModel.insertEvent(date, title, color)
        }

        supportFragmentManager.setFragmentResultListener(ChooseDateDialog.REQUEST_KEY, this) { _, bundle ->
            val month = bundle.getInt(ChooseDateDialog.MONTH)
            val year = bundle.getInt(ChooseDateDialog.YEAR)
            val calendar = binding.viewPager.getCurrentCalendar()!!
            val diff = (12 * year + month) - (12 * calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH))
            binding.viewPager.moveItemBy(diff, true)
        }

        binding.btnAddEvent.setOnClickListener {
            EventDialog().show(supportFragmentManager, null)
        }

        binding.rvEvents.adapter = eventAdapter

        updateMonthTitle(Calendar.getInstance())

        binding.btnSelectRange.setOnClickListener {
            isSelectRange = !isSelectRange
            binding.btnSelectRange.setText(if (isSelectRange) R.string.cancel_range else R.string.select_range)
            if (!isSelectRange) {
                adapter.selectStartDate = null
                adapter.selectEndDate = null
            }
        }

        viewModel.getAllEvents()

        observe()
    }

    private fun observe() {
        viewModel.events.observe(this) {
            adapter.events = it
            eventAdapter.submitList(it)
        }
    }

    override fun onRemove(event: Event) {
        viewModel.removeEvent(event.id)
    }

    private fun updateMonthTitle(calendar: Calendar) {
        binding.calendarTitle.text = sdf.format(calendar.time)
    }

}