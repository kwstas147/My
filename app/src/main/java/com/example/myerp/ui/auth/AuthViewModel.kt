package com.example.myerp.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.AppDatabase
import com.example.myerp.data.User
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    fun registerUser(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                userDao.registerUser(User(username, password))
                onSuccess()
            } catch (e: Exception) {
                onError("Registration failed: ${e.message}")
            }
        }
    }

    fun loginUser(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByUsername(username)
                if (user != null) {
                    val (storedHash, salt) = user.password.split(":")
                    val enteredHash = hashPassword(password, salt)
                    if (enteredHash == storedHash) {
                        onSuccess()
                    } else {
                        onError("Invalid username or password")
                    }
                } else {
                    onError("Invalid username or password")
                }
            } catch (e: Exception) {
                onError("Login failed: ${e.message}")
            }
        }
    }

    private fun hashPassword(password: String, salt: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hash = digest.digest((password + salt).toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}