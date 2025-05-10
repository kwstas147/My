package com.example.myerp.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.CommentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SlideshowViewModel(private val commentDao: CommentDao) : ViewModel() {

    // Μεταβλητή για προσωρινή αποθήκευση του σχολίου που διαγράφηκε
    private var lastDeletedComment: Comment? = null

    fun getCommentsByDateFlow(date: String): Flow<List<Comment>> {
        println("Querying comments for date (Flow): $date") // Debug log
        return commentDao.getCommentsByDateFlow(date)
    }

    fun saveFieldDataToCalendar(fieldName: String, amount: Double, oldBalance: Double, newBalance: Double, date: String) {
        viewModelScope.launch {
            val commentText = "Field: $fieldName, Amount: $amount, Old Balance: $oldBalance, New Balance: $newBalance"
            println("Saving field data to calendar: $commentText on date: $date") // Debug log
            commentDao.insertComment(Comment(text = commentText, date = date))
        }
    }

    fun addComment(text: String, date: String) {
        viewModelScope.launch {
            println("Inserting comment: text=$text, date=$date") // Debug log
            commentDao.insertComment(Comment(text = text, date = date))
            println("Comment inserted successfully.") // Debug log
        }
    }

    // Τροποποιημένη deleteComment function
    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            println("Preparing to delete comment: $comment") // Debug log
            // Αποθηκεύουμε προσωρινά το σχόλιο πριν το διαγράψουμε
            lastDeletedComment = comment
            commentDao.deleteComment(comment)
            println("Comment deleted (temporarily).") // Debug log
        }
    }

    // Νέα function για την αναίρεση διαγραφής
    fun undoDeleteComment() {
        viewModelScope.launch {
            lastDeletedComment?.let { comment ->
                println("Undoing delete for comment: $comment") // Debug log
                commentDao.insertComment(comment) // Επαναφέρουμε το σχόλιο
                lastDeletedComment = null // Καθαρίζουμε την προσωρινή αποθήκευση
                println("Undo successful. Comment re-inserted.") // Debug log
            }
        }
    }
}