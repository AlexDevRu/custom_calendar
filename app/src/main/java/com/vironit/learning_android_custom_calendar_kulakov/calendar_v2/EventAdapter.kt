package com.vironit.learning_android_custom_calendar_kulakov.calendar_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ItemEventBinding
import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(
    private val listener : Listener
): ListAdapter<Date, EventAdapter.EventViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : ItemCallback<Date>() {
            override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
                return DateUtils.isSameDay(oldItem, newItem)
            }

            override fun areItemsTheSame(oldItem: Date, newItem: Date): Boolean {
                return DateUtils.isSameDay(oldItem, newItem)
            }
        }
    }

    interface Listener {
        fun onRemove(date: Date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EventViewHolder(
        private val binding : ItemEventBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        private var date: Date? = null

        init {
            binding.btnRemove.setOnClickListener(this)
        }

        fun bind(date: Date) {
            this.date = date
            binding.tvEvent.text = sdf.format(date)
        }

        override fun onClick(view: View?) {
            date?.let {
                listener.onRemove(it)
            }
        }

    }

}