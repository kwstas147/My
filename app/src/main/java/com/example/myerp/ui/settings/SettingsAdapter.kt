package com.example.myerp.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myerp.databinding.ItemSettingsFieldBinding
import com.example.myerp.data.FieldData
import androidx.recyclerview.widget.DiffUtil

class SettingsAdapter(
    private val onRemoveClick: (FieldData) -> Unit
) : ListAdapter<FieldData, SettingsAdapter.SettingsViewHolder>(FieldDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val binding = ItemSettingsFieldBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SettingsViewHolder(private val binding: ItemSettingsFieldBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fieldData: FieldData) {
            binding.fieldName.text = fieldData.name
            binding.removeButton.setOnClickListener {
                onRemoveClick(fieldData)
            }
        }
    }
}

class FieldDiffCallback : DiffUtil.ItemCallback<FieldData>() {
    override fun areItemsTheSame(oldItem: FieldData, newItem: FieldData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FieldData, newItem: FieldData): Boolean {
        return oldItem == newItem
    }
}