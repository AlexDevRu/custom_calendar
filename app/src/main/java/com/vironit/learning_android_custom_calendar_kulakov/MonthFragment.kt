package com.vironit.learning_android_custom_calendar_kulakov

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.SimpleItemAnimator
import com.vironit.learning_android_custom_calendar_kulakov.databinding.FragmentMonthBinding
import java.util.*

class MonthFragment : Fragment() {

    private var binding: FragmentMonthBinding? = null

    private val adapter = MonthAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.root.adapter = adapter
        val shift = arguments?.getInt(SHIFT) ?: 0
        updateUI(shift)
    }

    fun updateUI(shift: Int) {
        val days = mutableListOf<Day>()

        //days.add(Day(shift, true))
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        calendar.add(Calendar.MONTH, shift)
        val month = calendar.get(Calendar.MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, dayCount)
        val lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        Log.d("asd", "onViewCreated: $shift $month $dayCount $firstDayOfWeek $lastDayOfWeek")

        calendar.add(Calendar.MONTH, -1)
        val prevDayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        (0 until (firstDayOfWeek - 1)).forEach {
            days.add(0, Day(prevDayCount - it, false))
        }
        (1..dayCount).forEach {
            days.add(Day(it, true))
        }
        (1 .. (7 - lastDayOfWeek)).forEach {
            days.add(Day(it, false))
        }

        adapter.submitList(days)
    }

    companion object {
        private const val TAG = "MonthFragment"
        private const val SHIFT = "SHIFT"

        fun createInstance(shift: Int) : MonthFragment {
            val fragment = MonthFragment()
            fragment.arguments = bundleOf(SHIFT to shift)
            return fragment
        }
    }

}