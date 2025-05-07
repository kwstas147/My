package com.example.myerp.data

import androidx.annotation.WorkerThread
import com.example.myerp.ui.slideshow.Comment
import kotlinx.coroutines.flow.Flow

class CommentRepository(private val commentDao: CommentDao) {

    val allComments: Flow<List<Comment>> = commentDao.getAllComments() // Χρήση της νέας μεθόδου

    @WorkerThread
    suspend fun insert(comment: Comment) {
        commentDao.insertComment(comment) // Διόρθωση: Χρήση της σωστής μεθόδου
    }

    @WorkerThread
    suspend fun delete(comment: Comment) {
        commentDao.deleteComment(comment) // Προσθήκη μεθόδου διαγραφής
    }
}
