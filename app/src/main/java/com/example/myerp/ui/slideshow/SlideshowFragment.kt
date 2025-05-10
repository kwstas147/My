package com.example.myerp.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myerp.MyApp
import com.example.myerp.databinding.FragmentSlideshowBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var adapter: CommentAdapter

    // Συνάρτηση για ενημέρωση σχολίων με βάση την επιλεγμένη ημερομηνία (moved to class level)
    private fun updateCommentsForDate(selectedDate: String) {
        println("Ανάκτηση σχολίων για την ημερομηνία: $selectedDate") // Debug log
        lifecycleScope.launch {
            slideshowViewModel.getCommentsByDateFlow(selectedDate).collect { comments ->
                println("Σχόλια που ανακτήθηκαν για την ημερομηνία $selectedDate: $comments") // Debug log
                adapter.updateComments(comments) // Ενημέρωση του adapter με τα νέα σχόλια
                binding.commentsRecyclerView.visibility = if (comments.isEmpty()) {
                    println("Δεν υπάρχουν σχόλια για εμφάνιση.") // Debug log
                    View.GONE
                } else {
                    println("Εμφάνιση RecyclerView με σχόλια.") // Debug log
                    View.VISIBLE
                }
            }
        }
    }

    // Συνάρτηση για κοινοποίηση σχολίου (moved to class level)
    private fun shareComment(commentText: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, commentText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share comment via"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Δημιουργία του AppDatabase και του CommentDao
        val database = (requireActivity().application as MyApp).database
        val commentDao = database.commentDao()

        // Δημιουργία του ViewModel με χρήση του Factory
        slideshowViewModel = ViewModelProvider(
            this,
            SlideshowViewModelFactory(commentDao)
        )[SlideshowViewModel::class.java]

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Αναφορές στα στοιχεία του layout
        val currentDateTextView = binding.currentDate
        val datePicker = binding.datePicker
        val commentInput = binding.commentInput
        val addCommentButton = binding.addCommentButton
        val recyclerView = binding.commentsRecyclerView

        // Ρύθμιση του RecyclerView με LinearLayoutManager
        adapter = CommentAdapter(mutableListOf(), { comment ->
            // Καλώντας τη deleteComment από το ViewModel αποθηκεύουμε προσωρινά το σχόλιο
            slideshowViewModel.deleteComment(comment)

            // Εμφάνιση του Snackbar με επιλογή αναίρεσης
            Snackbar.make(
                requireView(), // Το View στο οποίο θα "κολλήσει" το Snackbar
                "Σχόλιο διεγράφη", // Το μήνυμα που θα εμφανιστεί
                Snackbar.LENGTH_LONG // Πόση ώρα θα εμφανίζεται το Snackbar
            ).apply {
                setAction("Αναίρεση") {
                    // Όταν ο χρήστης πατήσει "Αναίρεση"
                    slideshowViewModel.undoDeleteComment()
                    // Η λίστα θα ενημερωθεί αυτόματα μέσω του Flow
                }
                show() // Εμφάνιση του Snackbar
            }

            // Δεν χρειάζεται να καλέσετε updateCommentsForDate(currentDateTextView.text.toString()) εδώ
            // γιατί το Flow θα εκπέμψει τη νέα λίστα μετά τη διαγραφή (ή την αναίρεση)
            // και ο adapter θα ενημερωθεί αυτόματα από το collect block παραπάνω.

        }, { comment ->
            shareComment(comment.text)
        })
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        // Αρχική ενημέρωση σχολίων
        val initialDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        updateCommentsForDate(initialDate)

        // Listener για αλλαγή ημερομηνίας
        datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
            currentDateTextView.text = selectedDate
            updateCommentsForDate(selectedDate)
        }

        // Listener για προσθήκη σχολίου
        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank()) {
                slideshowViewModel.addComment(commentText, currentDateTextView.text.toString())
                commentInput.text.clear()
                // Η λίστα θα ενημερωθεί αυτόματα μέσω του Flow μετά την προσθήκη
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}