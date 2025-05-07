package com.example.myerp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myerp.MyApp
import com.example.myerp.databinding.FragmentSlideshowBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // Αυτή η ιδιότητα είναι έγκυρη μόνο μεταξύ των μεθόδων onCreateView και onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Δημιουργία του AppDatabase και του CommentDao
        val database = (requireActivity().application as MyApp).database
        val commentDao = database.commentDao()

        // Δημιουργία του ViewModel με χρήση του Factory
        val slideshowViewModel = ViewModelProvider(
            this,
            SlideshowViewModelFactory(commentDao)
        ).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Αναφορές στα στοιχεία του layout
        val currentDateTextView = binding.currentDate
        val datePicker = binding.datePicker
        val commentInput = binding.commentInput
        val addCommentButton = binding.addCommentButton
        val recyclerView = binding.commentsRecyclerView

        // Ρύθμιση του RecyclerView με τον adapter
        val adapter = CommentAdapter(mutableListOf()) { comment ->
            println("Διαγραφή σχολίου με ID: ${comment.id}") // Debug log
            slideshowViewModel.deleteComment(comment) // Περνάμε το αντικείμενο Comment
        }
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true) // Βελτιστοποίηση του RecyclerView

        // Ρύθμιση της τρέχουσας ημερομηνίας
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        currentDateTextView.text = dateFormat.format(calendar.time)

        // Συνάρτηση για ενημέρωση σχολίων με βάση την επιλεγμένη ημερομηνία
        fun updateCommentsForDate(selectedDate: String) {
            println("Ανάκτηση σχολίων για την ημερομηνία: $selectedDate") // Debug log
            slideshowViewModel.getCommentsByDate(selectedDate).observe(viewLifecycleOwner) { comments ->
                println("Σχόλια που ανακτήθηκαν για την ημερομηνία $selectedDate: $comments") // Debug log
                adapter.updateComments(comments) // Ενημέρωση του adapter με τα νέα σχόλια
                recyclerView.visibility = if (comments.isEmpty()) {
                    println("Δεν υπάρχουν σχόλια για εμφάνιση.") // Debug log
                    View.GONE
                } else {
                    println("Εμφάνιση RecyclerView με σχόλια.") // Debug log
                    View.VISIBLE
                }
            }
        }

        // Αρχική ενημέρωση σχολίων
        val initialDate = dateFormat.format(calendar.time)
        println("Αρχική ημερομηνία για σχόλια: $initialDate") // Debug log
        updateCommentsForDate(initialDate)

        // Listener για αλλαγή ημερομηνίας
        datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth) // Εξασφαλίζουμε σωστή μορφή ημερομηνίας
            println("Η ημερομηνία άλλαξε σε: $selectedDate") // Debug log
            currentDateTextView.text = selectedDate
            updateCommentsForDate(selectedDate)
        }

        // Listener για προσθήκη σχολίου
        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank()) {
                println("Προσθήκη σχολίου: $commentText για την ημερομηνία: ${currentDateTextView.text}") // Debug log
                slideshowViewModel.addComment(commentText, currentDateTextView.text.toString())
                commentInput.text.clear()
            } else {
                println("Το πεδίο σχολίου είναι κενό, δεν προστέθηκε σχόλιο.") // Debug log
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
