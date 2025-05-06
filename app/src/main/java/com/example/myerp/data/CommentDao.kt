package com.example.myerp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myerp.ui.slideshow.Comment

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE date = :date ORDER BY id DESC")
    fun getCommentsByDate(date: String): LiveData<List<Comment>>

    @Insert
    suspend fun insertComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)
}
