package com.example.myerp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myerp.ui.slideshow.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE date = :date ORDER BY id DESC")
    fun getCommentsByDate(date: String): LiveData<List<Comment>>

    @Query("SELECT * FROM comments ORDER BY id DESC")
    fun getAllComments(): Flow<List<Comment>> // Νέα μέθοδος για επιστροφή όλων των σχολίων

    @Insert
    suspend fun insertComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)
}
