package com.example.myerp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.CommentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SlideshowViewModel(private val commentDao: CommentDao) : ViewModel() {

    // SlideshowViewModel.kt
    fun getCommentsByDate(date: String): LiveData<List<Comment>> {
        println("Querying comments for date: $date") // Debug log
        val comments = commentDao.getCommentsByDate(date)
        comments.observeForever { println("Comments fetched from database: $it") } // Debug log
        return comments
    }

    fun getCommentsByDateFlow(date: String): Flow<List<Comment>> {
        println("Querying comments for date (Flow): $date") // Debug log
        return commentDao.getCommentsByDateFlow(date)
    }

    fun addComment(text: String, date: String) {
        viewModelScope.launch {
            println("Inserting comment: text=$text, date=$date") // Debug log
            commentDao.insertComment(Comment(text = text, date = date))
            println("Comment inserted successfully.") // Debug log
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            println("Deleting comment: $comment") // Debug log
            commentDao.deleteComment(comment)
            println("Comment deleted successfully.") // Debug log
        }
    }
}
