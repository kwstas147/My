package com.example.myerp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.myerp.MainActivity
import com.example.myerp.R
import com.example.myerp.data.AppDatabase
import com.example.myerp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import java.util.concurrent.Executor

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

        // Check for biometric support
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            binding.biometricButton.visibility = View.VISIBLE
            setupBiometricPrompt()
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                binding.errorText.text = getString(R.string.error_fill_all_fields)
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
                        binding.errorText.text = getString(R.string.error_invalid_credentials)
                    }
                } else {
                    binding.errorText.text = getString(R.string.error_invalid_credentials)
                }
            }
        }

        binding.registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun setupBiometricPrompt() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Navigate to MainActivity on successful authentication
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                binding.errorText.text = getString(R.string.error_biometric_auth_failed)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                binding.errorText.text = getString(R.string.error_biometric_auth_failed)
            }
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_prompt_title))
            .setSubtitle(getString(R.string.biometric_prompt_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_prompt_cancel))
            .build()

        binding.biometricButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}

private fun hashPassword(password: String, salt: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest((password + salt).toByteArray())
    return hash.joinToString("") { "%02x".format(it) }
}
