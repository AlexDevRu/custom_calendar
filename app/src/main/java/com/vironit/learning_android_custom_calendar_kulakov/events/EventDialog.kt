package com.vironit.learning_android_custom_calendar_kulakov.events

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.vironit.learning_android_custom_calendar_kulakov.R
import com.vironit.learning_android_custom_calendar_kulakov.databinding.DialogAddEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventDialog : DialogFragment(), DialogInterface.OnClickListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var binding : DialogAddEventBinding

    private var wedding = false
    private var birthday = false
    private var graduation = false

    private var date = ""

    private val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogAddEventBinding.inflate(layoutInflater)

        binding.btnAddDate.setOnClickListener(this)

        binding.chip1.setOnCheckedChangeListener(this)
        binding.chip2.setOnCheckedChangeListener(this)
        binding.chip3.setOnCheckedChangeListener(this)

        Calendar.getInstance().also { calendar ->
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            date = "$year $month $dayOfMonth"
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_event)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok, this)
            .create()
    }

    override fun onClick(view: View?) {
        if (view == binding.btnAddDate) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            return
        }
    }

    override fun onCheckedChanged(btn: CompoundButton?, value: Boolean) {
        when (btn) {
            binding.chip1 -> wedding = value
            binding.chip2 -> birthday = value
            binding.chip3 -> graduation = value
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        date = "$year $month $dayOfMonth"
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        binding.date.text = sdf.format(calendar.time)
        binding.btnAddDate.isVisible = false
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        setFragmentResult("EventDialog", bundleOf(
            DATE to date,
            WEDDING to wedding,
            BIRTHDAY to birthday,
            GRADUATION to graduation,
        ))
        dismiss()
    }

    companion object {
        const val DATE = "date"
        const val WEDDING = "WEDDING"
        const val BIRTHDAY = "BIRTHDAY"
        const val GRADUATION = "GRADUATION"
    }

}