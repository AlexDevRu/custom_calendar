package com.vironit.learning_android_custom_calendar_kulakov

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vironit.learning_android_custom_calendar_kulakov.databinding.ItemDayBinding

class MonthAdapter: RecyclerView.Adapter<MonthAdapter.DayViewHolder>()/*ListAdapter<Day, MonthAdapter.DayViewHolder>(DIFF_UTIL)*/ {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Day>() {
            override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    private var list: List<Day> = emptyList()

    fun submitList(list: List<Day>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getItem(position: Int) = list[position]

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DayViewHolder(
        private val binding: ItemDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Day) {
            val colorRes = if (day.currentMonth) R.color.black else R.color.grey
            binding.textView.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
            binding.textView.text = day.day.toString()
        }
    }

}