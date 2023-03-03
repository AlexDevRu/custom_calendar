package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.vironit.learning_android_custom_calendar_kulakov.R
import com.vironit.learning_android_custom_calendar_kulakov.databinding.DialogChooseDateBinding
import java.text.DateFormatSymbols
import java.util.*

class ChooseDateDialog : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var binding : DialogChooseDateBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogChooseDateBinding.inflate(layoutInflater)

        val time = arguments?.getLong(TIME) ?: 0L
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)

        val months = DateFormatSymbols.getInstance().months
        binding.monthPicker.minValue = 1
        binding.monthPicker.maxValue = months.size
        binding.monthPicker.displayedValues = months
        binding.monthPicker.value = calendar.get(Calendar.MONTH) + 1

        binding.yearPicker.setFormatter { it.toString() }

        binding.yearPicker.value = calendar.get(Calendar.YEAR)

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.choose_date)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok, this)
            .create()
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        val month = binding.monthPicker.value - 1
        val year = binding.yearPicker.value
        setFragmentResult(REQUEST_KEY, bundleOf(MONTH to month, YEAR to year))
        dismiss()
    }

    companion object {
        private const val TIME = "TIME"
        const val REQUEST_KEY = "ChooseDateDialog"
        const val MONTH = "MONTH"
        const val YEAR = "YEAR"

        fun createInstance(time: Long) : ChooseDateDialog {
            val dialog = ChooseDateDialog()
            dialog.arguments = bundleOf(TIME to time)
            return dialog
        }
    }

}