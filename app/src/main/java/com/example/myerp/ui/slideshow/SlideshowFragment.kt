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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel = ViewModelProvider(
            this,
            SlideshowViewModelFactory((requireActivity().application as MyApp).database.commentDao())
        ).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDateTextView = binding.currentDate
        val datePicker = binding.datePicker
        val commentInput = binding.commentInput
        val addCommentButton = binding.addCommentButton
        val recyclerView = binding.commentsRecyclerView

        val adapter = CommentAdapter(mutableListOf()) { id ->
            println("Deleting comment with ID: $id") // Debug log
            slideshowViewModel.deleteComment(id)
        }
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true) // Ensure RecyclerView optimizations

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        currentDateTextView.text = dateFormat.format(calendar.time)

        // Ενημέρωση σχολίων με βάση την επιλεγμένη ημερομηνία
        fun updateCommentsForDate(selectedDate: String) {
            println("Fetching comments for date: $selectedDate") // Debug log
            slideshowViewModel.getCommentsByDate(selectedDate).observe(viewLifecycleOwner) { comments ->
                println("Comments fetched for date $selectedDate: $comments") // Debug log
                adapter.updateComments(comments)
                if (comments.isEmpty()) {
                    println("No comments found for date: $selectedDate") // Debug log
                } else {
                    println("Comments successfully updated in RecyclerView.") // Debug log
                }
            }
        }

        // Αρχική ενημέρωση σχολίων
        val initialDate = dateFormat.format(calendar.time)
        println("Initial date for comments: $initialDate") // Debug log
        updateCommentsForDate(initialDate)

        // Listener για αλλαγή ημερομηνίας
        datePicker.setOnDateChangedListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth) // Εξασφαλίζουμε σωστή μορφή ημερομηνίας
            println("Date changed to: $selectedDate") // Debug log
            currentDateTextView.text = selectedDate
            updateCommentsForDate(selectedDate)
        }

        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank()) {
                println("Adding comment: $commentText for date: ${currentDateTextView.text}") // Debug log
                slideshowViewModel.addComment(commentText, currentDateTextView.text.toString())
                commentInput.text.clear()
            } else {
                println("Comment input is blank, not adding comment.") // Debug log
            }
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
