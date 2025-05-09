package com.example.myerp.ui.gallery

import android.app.AlertDialog
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
import android.widget.EditText
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myerp.R
import com.example.myerp.data.AppDatabase
import com.example.myerp.databinding.FragmentGalleryBinding
import com.example.myerp.data.LogEntry
import com.example.myerp.data.LogEntryAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val galleryViewModel: GalleryViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        GalleryViewModel.provideFactory(database.logEntryDao(), database.fieldDao())
    }
    private lateinit var adapter: LogEntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView
        setupRecyclerView()

        // Load log entries and fields
        galleryViewModel.loadLogEntries()
        galleryViewModel.loadFields()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe log entries
        galleryViewModel.logEntries.observe(viewLifecycleOwner) { logEntries ->
            println("Observed log entries: $logEntries") // Debugging line
            if (logEntries.isEmpty()) {
                println("No log entries to display in the gallery.") // Debugging line
            }
            adapter.submitList(logEntries)
        }

        // Observe fields
        galleryViewModel.fields.observe(viewLifecycleOwner) { fields ->
            println("Observed fields: $fields") // Debugging line
            // Handle fields display logic here if needed
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
                        if (logEntries.isEmpty()) {
                            println("No log entries to share.") // Debugging line
                        }
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
        adapter = LogEntryAdapter(galleryViewModel) { logEntry ->
            showCommentDialog(logEntry) // Handle comment button click
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@GalleryFragment.adapter
        }
    }

    private fun setupButtons() {
        // Filter log entries
        binding.filterInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                galleryViewModel.filterLogEntries(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
            }
        })
    }

    private fun showCommentDialog(logEntry: LogEntry) {
        val editText = EditText(requireContext())
        editText.hint = "Enter your comment"

        AlertDialog.Builder(requireContext())
            .setTitle("Add Comment")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val comment = editText.text.toString()
                if (comment.isNotBlank()) {
                    galleryViewModel.updateLogEntryComment(logEntry, comment)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
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
