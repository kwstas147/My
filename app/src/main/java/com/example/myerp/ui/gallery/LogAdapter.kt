package com.example.myerp.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myerp.R
import com.example.myerp.data.LogEntry

class LogAdapter(private val logEntries: List<LogEntry>) :
    RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fieldName: TextView = itemView.findViewById(R.id.fieldName)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val oldBalance: TextView = itemView.findViewById(R.id.oldBalance)
        val newBalance: TextView = itemView.findViewById(R.id.newBalance)
        val comment: TextView = itemView.findViewById(R.id.comment)
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
}