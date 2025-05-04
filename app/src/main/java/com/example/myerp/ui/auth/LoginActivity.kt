package com.example.myerp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myerp.MainActivity
import com.example.myerp.data.AppDatabase
import com.example.myerp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if a user exists
        val userDao = AppDatabase.getDatabase(this).userDao()
        val isUserRegistered = runBlocking { userDao.getUserCount() > 0 }

        if (!isUserRegistered) {
            // Navigate to RegisterActivity if no user is registered
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                binding.errorText.text = "Please fill in all fields"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = userDao.getUserByUsername(username)
                if (user != null) {
                    val (storedHash, salt) = user.password.split(":")
                    val enteredHash = hashPassword(password, salt)
                    if (enteredHash == storedHash) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        binding.errorText.text = "Invalid username or password"
                    }
                } else {
                    binding.errorText.text = "Invalid username or password"
                }
            }
        }


        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

private fun hashPassword(password: String, salt: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest((password + salt).toByteArray())
    return hash.joinToString("") { "%02x".format(it) }
}


