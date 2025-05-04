package com.example.myerp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myerp.data.LogEntry
import com.example.myerp.databinding.ItemLogEntryBinding

class LogEntryAdapter : ListAdapter<LogEntry, LogEntryAdapter.LogEntryViewHolder>(LogEntryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogEntryViewHolder {
        val binding = ItemLogEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogEntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogEntryViewHolder, position: Int) {
        val logEntry = getItem(position)
        holder.bind(logEntry)
    }

    class LogEntryViewHolder(private val binding: ItemLogEntryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logEntry: LogEntry) {
            binding.fieldNameTextView.text = logEntry.fieldName
            binding.amountTextView.text = logEntry.amount.toString()
            binding.oldBalanceTextView.text = logEntry.oldBalance.toString()
            binding.newBalanceTextView.text = logEntry.newBalance.toString()
            binding.commentTextView.text = logEntry.comment ?: "" // Display comment or empty string if null
            binding.timestampTextView.text = logEntry.timestamp.toString() // You may format this as needed
        }
    }
}

class LogEntryDiffCallback : DiffUtil.ItemCallback<LogEntry>() {
    override fun areItemsTheSame(oldItem: LogEntry, newItem: LogEntry): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: LogEntry, newItem: LogEntry): Boolean {
        return oldItem == newItem
    }
}