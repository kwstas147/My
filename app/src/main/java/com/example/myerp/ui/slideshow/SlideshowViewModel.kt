package com.example.myerp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myerp.data.CommentDao
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SlideshowViewModel(private val commentDao: CommentDao) : ViewModel() {

    // SlideshowViewModel.kt
    fun getCommentsByDate(date: String): LiveData<List<Comment>> {
        println("Querying comments for date: $date") // Debug log
        return commentDao.getCommentsByDate(date).also {
            println("LiveData for comments fetched.") // Debug log
        }
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
            commentDao.deleteComment(comment)
        }
    }
}
