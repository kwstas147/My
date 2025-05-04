package com.example.myerp.ui.gallery

import android.content.Intent
import android.os.Bundle
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
import com.example.myerp.ui.adapters.LogEntryAdapter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var logEntryAdapter: LogEntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        galleryViewModel = ViewModelProvider(
            this,
            GalleryViewModelFactory(requireActivity().application)
        )[GalleryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Κλήση για φόρτωση δεδομένων
        galleryViewModel.loadLogEntries()

        // Initialize RecyclerView
        setupRecyclerView()

        // Observe log entries
        galleryViewModel.logEntries.observe(viewLifecycleOwner) { logEntries ->
            logEntryAdapter.submitList(logEntries)
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
    }

    private fun setupRecyclerView() {
        logEntryAdapter = LogEntryAdapter()
        binding.logEntriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = logEntryAdapter
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