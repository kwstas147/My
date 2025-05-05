package com.example.myerp.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myerp.R
import com.example.myerp.data.LogEntry

class LogEntryAdapter(private val galleryViewModel: GalleryViewModel) : RecyclerView.Adapter<LogEntryAdapter.LogViewHolder>() {

    private val logEntries = mutableListOf<LogEntry>()

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fieldName: TextView = itemView.findViewById(R.id.fieldNameTextView)
        val amount: TextView = itemView.findViewById(R.id.amountTextView)
        val oldBalance: TextView = itemView.findViewById(R.id.oldBalanceTextView)
        val newBalance: TextView = itemView.findViewById(R.id.newBalanceTextView)
        val comment: TextView = itemView.findViewById(R.id.commentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log_entry, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val entry = logEntries[position]
        holder.fieldName.text = entry.fieldName
        holder.amount.text = "Ποσό: ${entry.amount}"
        holder.oldBalance.text = "Παλιά Τιμή: ${entry.oldBalance}"
        holder.newBalance.text = "Νέα Τιμή: ${entry.newBalance}"
        holder.comment.text = "Σχόλιο: ${entry.comment ?: "Χωρίς σχόλιο"}"
    }

    override fun getItemCount(): Int = logEntries.size

    fun submitList(newEntries: List<LogEntry>) {
        logEntries.clear()
        logEntries.addAll(newEntries)
        notifyDataSetChanged()
    }
}
