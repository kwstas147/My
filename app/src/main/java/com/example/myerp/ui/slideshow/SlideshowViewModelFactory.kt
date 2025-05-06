package com.example.myerp.ui.slideshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myerp.data.CommentDao

class SlideshowViewModelFactory(private val commentDao: CommentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlideshowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlideshowViewModel(commentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}