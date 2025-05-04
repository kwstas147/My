package com.example.myerp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myerp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            authViewModel.loginUser(username, password, {
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
            }, { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            })
        }

        binding.registerLink.setOnClickListener {
            // Navigate to RegisterFragment
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}