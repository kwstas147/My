package com.example.myerp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.myerp.R
import com.example.myerp.ui.gallery.GalleryViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import kotlin.sequences.ifEmpty
import kotlin.text.format
import kotlin.toString

class LogEntryAdapter(
    private val galleryViewModel: GalleryViewModel,
    private val onCommentClick: (LogEntry) -> Unit // Callback for comment action
) : ListAdapter<LogEntry, LogEntryAdapter.LogEntryViewHolder>(LogEntryDiffCallback()) {

    class LogEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fieldNameTextView: TextView = itemView.findViewById(R.id.fieldNameTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val oldBalanceTextView: TextView = itemView.findViewById(R.id.oldBalanceTextView)
        val newBalanceTextView: TextView = itemView.findViewById(R.id.newBalanceTextView)
        val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val commentButton: Button = itemView.findViewById(R.id.commentButton) // Add comment button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogEntryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_log_entry, parent, false)
        return LogEntryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LogEntryViewHolder, position: Int) {
        val currentEntry = getItem(position)
        holder.fieldNameTextView.text = currentEntry.fieldName
        holder.amountTextView.text = currentEntry.amount.toString()
        holder.oldBalanceTextView.text = currentEntry.oldBalance.toString()
        holder.newBalanceTextView.text = currentEntry.newBalance.toString()
        holder.commentTextView.text = currentEntry.comment.ifEmpty { "No comment" }

        // Format the timestamp into a readable date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentEntry.timestamp))
        holder.timestampTextView.text = formattedDate

        holder.deleteButton.setOnClickListener {
            galleryViewModel.deleteLogEntry(currentEntry)
        }

        holder.commentButton.setOnClickListener {
            onCommentClick(currentEntry) // Trigger the comment action
        }
    }
}
