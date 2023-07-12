package com.vironit.learning_android_custom_calendar_kulakov.events

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
): ListAdapter<Event, EventAdapter.EventViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : ItemCallback<Event>() {
            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    interface Listener {
        fun onRemove(event: Event)
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

        private val sdf = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
        private var event: Event? = null

        init {
            binding.btnRemove.setOnClickListener(this)
        }

        fun bind(event: Event) {
            this.event = event
            binding.tvEvent.text = "${event.title}\n${sdf.format(event.calendar.time)}"
        }

        override fun onClick(view: View?) {
            event?.let {
                listener.onRemove(it)
            }
        }

    }

}