package com.example.myerp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myerp.databinding.ItemFieldBinding
import com.example.myerp.data.FieldData
import com.example.myerp.R

class HomeAdapter(
    private val onAddClick: (FieldData, Double) -> Unit,
    private val onRemoveClick: (FieldData, Double) -> Unit
) : ListAdapter<FieldData, HomeAdapter.FieldViewHolder>(FieldDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        val binding = ItemFieldBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FieldViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FieldDiffCallback : DiffUtil.ItemCallback<FieldData>() {
        override fun areItemsTheSame(oldItem: FieldData, newItem: FieldData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FieldData, newItem: FieldData): Boolean {
            return oldItem == newItem
        }
    }

    inner class FieldViewHolder(private val binding: ItemFieldBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fieldData: FieldData) {
            binding.fieldName.text = fieldData.name
            binding.fieldTotal.text = binding.root.context.getString(R.string.field_total, fieldData.total.toInt())

            binding.addButton.setOnClickListener {
                val value = binding.inputField.text.toString().toDoubleOrNull()
                if (value != null) {
                    onAddClick(fieldData, value)
                    binding.inputField.text.clear()
                }
            }

            binding.removeButton.setOnClickListener {
                val value = binding.inputField.text.toString().toDoubleOrNull()
                if (value != null) {
                    onRemoveClick(fieldData, value)
                    binding.inputField.text.clear()
                }
            }
        }
    }
}