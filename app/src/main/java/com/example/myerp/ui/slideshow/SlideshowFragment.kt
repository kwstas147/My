package com.example.myerp.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentDateTextView = binding.currentDate
        val datePicker = binding.datePicker
        val commentInput = binding.commentInput
        val addCommentButton = binding.addCommentButton
        val recyclerView = binding.commentsRecyclerView

        val adapter = CommentAdapter(mutableListOf()) { id ->
            slideshowViewModel.deleteComment(id)
        }
        recyclerView.adapter = adapter

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        currentDateTextView.text = dateFormat.format(calendar.time)

        addCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotBlank()) {
                slideshowViewModel.addComment(commentText)
                commentInput.text.clear()
            }
        }

        slideshowViewModel.comments.observe(viewLifecycleOwner) { comments ->
            adapter.notifyDataSetChanged()
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}