package com.example.myerp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.myerp.R
import com.example.myerp.ui.gallery.GalleryViewModel

class LogEntryAdapter(private val galleryViewModel: GalleryViewModel) :
    ListAdapter<LogEntry, LogEntryAdapter.LogEntryViewHolder>(LogEntryDiffCallback()) {

    class LogEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fieldNameTextView: TextView = itemView.findViewById(R.id.fieldNameTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val oldBalanceTextView: TextView = itemView.findViewById(R.id.oldBalanceTextView)
        val newBalanceTextView: TextView = itemView.findViewById(R.id.newBalanceTextView)
        val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogEntryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_log_entry, parent, false)
        return LogEntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogEntryViewHolder, position: Int) {
        val currentEntry = getItem(position)
        holder.fieldNameTextView.text = currentEntry.fieldName
        holder.amountTextView.text = currentEntry.amount.toString()
        holder.oldBalanceTextView.text = currentEntry.oldBalance.toString()
        holder.newBalanceTextView.text = currentEntry.newBalance.toString()
        holder.commentTextView.text = currentEntry.comment
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