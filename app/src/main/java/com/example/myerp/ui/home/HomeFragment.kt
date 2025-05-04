package com.example.myerp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myerp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupUI()
        observeViewModel()

        return binding.root
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HomeAdapter(
            onAddClick = { fieldData, value ->
                homeViewModel.adjustFieldValue(fieldData, value)
            },
            onRemoveClick = { fieldData, value ->
                homeViewModel.adjustFieldValue(fieldData, -value)
            }
        )
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        homeViewModel.fieldData.observe(viewLifecycleOwner) { fieldData ->
            (binding.recyclerView.adapter as HomeAdapter).submitList(fieldData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}