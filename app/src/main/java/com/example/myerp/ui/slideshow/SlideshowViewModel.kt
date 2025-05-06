package com.example.myerp.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _comments = MutableLiveData<MutableList<Comment>>(mutableListOf())
    val comments: LiveData<MutableList<Comment>> = _comments

    private var commentId = 0

    fun addComment(text: String) {
        val newComment = Comment(commentId++, text)
        _comments.value?.add(newComment)
        _comments.value = _comments.value // Trigger LiveData update
    }

    fun deleteComment(id: Int) {
        _comments.value = _comments.value?.filter { it.id != id }?.toMutableList()
    }
}