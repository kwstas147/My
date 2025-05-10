package com.example.myerp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myerp.ui.slideshow.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE date = :date ORDER BY id DESC")
    fun getCommentsByDate(date: String): LiveData<List<Comment>> // Ensure this matches the date format stored in the database

    @Query("SELECT * FROM comments WHERE date = :date ORDER BY id DESC")
    fun getCommentsByDateFlow(date: String): Flow<List<Comment>>

    @Query("SELECT * FROM comments ORDER BY id DESC")
    fun getAllComments(): Flow<List<Comment>>

    @Insert
    suspend fun insertComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)
}
