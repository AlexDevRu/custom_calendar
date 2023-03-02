package com.vironit.learning_android_custom_calendar_kulakov.calendar_v1

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarAdapter(fragment: FragmentActivity, private val fragments: List<MonthFragment>) : FragmentStateAdapter(fragment) {

    var center = 0
        private set

    fun onNext() {
        ++center
    }

    fun onPrev() {
        --center
    }

    fun updateUI() {
        fragments[0].updateUI(center - 1)
        fragments[1].updateUI(center)
        fragments[2].updateUI(center + 1)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        val shift = when (position) {
            0 -> center - 1
            1 -> center
            else -> center + 1
        }
        Log.d("asd", "createFragment: $position $shift")
        return fragments[position]
    }

    companion object {
        private const val TAG = "CalendarAdapter"
    }



}