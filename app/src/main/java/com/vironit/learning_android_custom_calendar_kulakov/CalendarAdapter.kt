package com.vironit.learning_android_custom_calendar_kulakov

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class CalendarAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    val shifts = mutableListOf(-1, 0, 1)

    fun onNext() {
        ++shifts[0]
        ++shifts[1]
        ++shifts[2]
    }

    fun onPrev() {
        --shifts[0]
        --shifts[1]
        --shifts[2]
    }

    override fun getItemCount(): Int {
        return shifts.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("asd", "createFragment: $position ${shifts[position]}")
        return MonthFragment.createInstance(shifts[position])
    }

    companion object {
        private const val TAG = "CalendarAdapter"
    }



}