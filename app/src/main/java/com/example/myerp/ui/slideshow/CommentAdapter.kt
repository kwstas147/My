package com.example.myerp.ui.slideshow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myerp.R
import androidx.recyclerview.widget.DiffUtil

class CommentAdapter(
    private var comments: MutableList<Comment>,
    private val onDelete: (Comment) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    
    fun updateComments(newComments: List<Comment>) {
        println("Updating comments in adapter: $newComments") // Debug log
        val diffCallback = CommentDiffCallback(comments, newComments)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        comments.clear()
        comments.addAll(newComments)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        println("Binding comment at position $position: $comment") // Debug log
        holder.commentText.text = comment.text
        holder.deleteButton.setOnClickListener {
            println("Delete button clicked for comment ID: ${comment.id}") // Debug log
            onDelete(comment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int = comments.size

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentText: TextView = itemView.findViewById(R.id.comment_text)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
    }
}
