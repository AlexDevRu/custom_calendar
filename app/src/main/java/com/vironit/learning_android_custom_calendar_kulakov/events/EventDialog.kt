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
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.vironit.learning_android_custom_calendar_kulakov.R
import com.vironit.learning_android_custom_calendar_kulakov.databinding.DialogAddEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventDialog : DialogFragment(), DialogInterface.OnClickListener, View.OnClickListener, DatePickerDialog.OnDateSetListener,
    ColorEnvelopeListener {

    private lateinit var binding : DialogAddEventBinding

    private var selectedColor = 0

    private var date = 0L

    private val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogAddEventBinding.inflate(layoutInflater)

        binding.btnAddDate.setOnClickListener(this)

        binding.colorView.setOnClickListener(this)

        date = Calendar.getInstance().timeInMillis

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
        } else if (view == binding.colorView) {
            ColorPickerDialog.Builder(requireContext())
                .setTitle(R.string.event_color)
                .setPositiveButton(getString(R.string.confirm), this as ColorEnvelopeListener)
                .setNegativeButton(getString(android.R.string.cancel), this)
                .attachAlphaSlideBar(false) // the default value is true.
                .attachBrightnessSlideBar(true)  // the default value is true.
                .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                .show()
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        date = calendar.timeInMillis
        binding.date.text = sdf.format(calendar.time)
        binding.btnAddDate.isVisible = false
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                setFragmentResult(REQUEST_KEY, bundleOf(
                    DATE to date,
                    TITLE to binding.etTitle.text?.toString().orEmpty(),
                    COLOR to selectedColor
                ))
            }
        }
        dialog?.dismiss()
    }

    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
        selectedColor = envelope!!.color
        binding.colorView.setBackgroundColor(selectedColor)
    }

    companion object {
        const val REQUEST_KEY = "EventDialog"
        const val DATE = "DATE"
        const val TITLE = "TITLE"
        const val COLOR = "COLOR"
    }

}