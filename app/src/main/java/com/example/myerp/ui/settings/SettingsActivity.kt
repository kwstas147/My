package com.example.myerp.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myerp.databinding.ActivitySettingsBinding
import com.example.myerp.ui.home.HomeViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = SettingsAdapter(
            onRemoveClick = { index ->
                homeViewModel.removeField(index)
            }
        )
        binding.recyclerView.adapter = adapter

        homeViewModel.fieldData.observe(this) { fields ->
            adapter.submitList(fields)
        }

        binding.addFieldButton.setOnClickListener {
            val fieldName = binding.fieldNameInput.text.toString()
            if (fieldName.isNotEmpty()) {
                homeViewModel.addField(fieldName)
                binding.fieldNameInput.text.clear()
            }
        }
    }
}