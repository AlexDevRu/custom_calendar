package com.vironit.learning_android_custom_calendar_kulakov

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ActivityMainBinding
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var calendarAdapter: CalendarAdapter

    private var prevFocusPage = 1
    private var focusPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarAdapter = CalendarAdapter(this)
        binding.viewPager.adapter = calendarAdapter

        val fragments = listOf(
            MonthFragment.createInstance(-1),
            MonthFragment.createInstance(0),
            MonthFragment.createInstance(1),
        )

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                focusPage = position

                Log.d("asd", "onPageSelected: $position")

                if (position > 1) {
                    calendarAdapter.onNext()
                } else if (position < 1) {
                    calendarAdapter.onPrev()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == SCROLL_STATE_IDLE) {
                    Log.d("asd", "onPageScrollStateChanged: SCROLL_STATE_IDLE")
                    binding.viewPager.setCurrentItem(1, false)
                    binding.viewPager.findFragmentAtPosition(supportFragmentManager, 0)?.let {
                        Log.d("asd", "onPageSelected: 1 fragment exist")
                        (it as MonthFragment).updateUI(calendarAdapter.shifts[0])
                    }
                    binding.viewPager.findFragmentAtPosition(supportFragmentManager, 1)?.let {
                        Log.d("asd", "onPageSelected: 2 fragment exist")
                        (it as MonthFragment).updateUI(calendarAdapter.shifts[1])
                    }
                    binding.viewPager.findFragmentAtPosition(supportFragmentManager, 2)?.let {
                        Log.d("asd", "onPageSelected: 3 fragment exist")
                        (it as MonthFragment).updateUI(calendarAdapter.shifts[2])
                    }
                    updateMonthTitle()
                }
            }
        })

        binding.viewPager.setCurrentItem(1, false)

        binding.btnMonthBack.setOnClickListener {
            binding.viewPager.setCurrentItem(0, true)
        }
        binding.btnMonthNext.setOnClickListener {
            binding.viewPager.setCurrentItem(2, true)
        }

        val shortWeekdays = DateFormatSymbols.getInstance(Locale.getDefault()).shortWeekdays.drop(1)
        val weekLettersTextViews = binding.llWeek.children.toList()

        shortWeekdays.zip(weekLettersTextViews).forEach { (date, textView) ->
            (textView as? TextView)?.text = date.first().toString()
        }
    }

    private fun updateMonthTitle() {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, calendarAdapter.shifts[1])

        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.calendarTitle.text = sdf.format(calendar.time)
    }

    fun ViewPager2.findFragmentAtPosition(
        fragmentManager: FragmentManager,
        position: Int
    ): Fragment? {
        return fragmentManager.findFragmentByTag("f$position")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}