package com.vironit.learning_android_custom_calendar_kulakov

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v1.CalendarAdapter
import com.vironit.learning_android_custom_calendar_kulakov.calendar_v1.MonthFragment
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ActivityMainBinding
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var calendarAdapter: CalendarAdapter

    private val fragments = listOf(
        MonthFragment.createInstance(-1),
        MonthFragment.createInstance(0),
        MonthFragment.createInstance(1),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.offscreenPageLimit = 2

        calendarAdapter = CalendarAdapter(this, fragments)
        binding.viewPager.adapter = calendarAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == SCROLL_STATE_IDLE) {
                    /*val position = binding.viewPager.currentItem
                    if (position > 1) {
                        calendarAdapter.onNext()
                    } else if (position < 1) {
                        calendarAdapter.onPrev()
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        calendarAdapter.updateUI()
                        binding.viewPager.setCurrentItem(1, false)
                        binding.viewPager.isUserInputEnabled = true
                    }, 50)*/
                    updateMonthTitle()
                } else if (state == SCROLL_STATE_SETTLING) {
                    //binding.viewPager.isUserInputEnabled = false
                }
            }
        })

        binding.viewPager.setCurrentItem(255, false)
        updateMonthTitle()
        calendarAdapter.updateUI()

        binding.btnMonthBack.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem - 1, true)
        }
        binding.btnMonthNext.setOnClickListener {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
        }

        val shortWeekdays = DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays.drop(1)
        val weekLettersTextViews = binding.llWeek.children.toList()

        shortWeekdays.zip(weekLettersTextViews).forEach { (date, textView) ->
            (textView as? TextView)?.text = date.first().toString()
        }

        binding.btnCalendarV2.setOnClickListener {
            val intent = Intent(this, CalendarV2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun updateMonthTitle() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, binding.viewPager.currentItem - 255)

        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.calendarTitle.text = sdf.format(calendar.time)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}