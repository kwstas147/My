package com.example.myerp.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myerp.R
import com.example.myerp.databinding.FragmentGalleryBinding
import com.example.myerp.data.LogEntry
import com.example.myerp.data.LogEntryAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var adapter: LogEntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        galleryViewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = LogEntryAdapter(galleryViewModel)
        binding.recyclerView.adapter = adapter

        galleryViewModel.logEntries.observe(viewLifecycleOwner) { logEntries ->
            adapter.submitList(logEntries)
        }
        galleryViewModel.loadLogEntries()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load log entries
        galleryViewModel.loadLogEntries()

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe log entries
        galleryViewModel.logEntries.observe(viewLifecycleOwner) { logEntries: List<LogEntry> ->
            adapter.submitList(logEntries)
        }

        // Add MenuProvider for the menu
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_gallery, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        val logEntries = galleryViewModel.logEntries.value.orEmpty()
                        shareLogEntries(logEntries)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

        // Add button listeners
        setupButtons()
    }

    private fun setupRecyclerView() {
        adapter = LogEntryAdapter(galleryViewModel) // Pass the ViewModel instance
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }
    }

    private fun setupButtons() {
        // Add a new log entry
        binding.addLogEntryButton.setOnClickListener {
            val newEntry = LogEntry(
                fieldName = "Sample Field",
                amount = 100.0,
                oldBalance = 500.0,
                newBalance = 600.0,
                comment = "Sample Comment",
                timestamp = System.currentTimeMillis() // Add timestamp
            )
            galleryViewModel.addLogEntry(newEntry)
        }

        // Filter log entries
        binding.filterInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                galleryViewModel.filterLogEntries(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Delete log entries by field name
        binding.deleteByFieldNameButton.setOnClickListener {
            galleryViewModel.deleteLogEntriesByFieldName("Sample Field")
        }
    }

    private fun shareLogEntries(logEntries: List<LogEntry>) {
        if (logEntries.isNotEmpty()) {
            val shareText = galleryViewModel.shareLogEntries(logEntries)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Log Entries"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}