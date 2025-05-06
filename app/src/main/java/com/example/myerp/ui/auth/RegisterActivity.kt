package com.example.myerp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myerp.data.AppDatabase
import com.example.myerp.data.User
import com.example.myerp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val userDao by lazy { AppDatabase.getDatabase(this).userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("Please fill in all fields")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showToast("Passwords do not match")
                return@setOnClickListener
            }

            val salt = generateSalt()
            val hashedPassword = hashPassword(password, salt)

            lifecycleScope.launch {
                try {
                    userDao.registerUser(User(username, "$hashedPassword:$salt"))
                    showToast("Registration successful")
                    navigateToLogin() // Navigate to login screen
                } catch (e: Exception) {
                    showToast("Registration failed: ${e.message}")
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    private fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(12) // Reduced size for efficiency
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    private fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest((password + salt).toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
