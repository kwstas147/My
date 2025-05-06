package com.example.myerp.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.example.myerp.data.FieldData

class FieldDiffCallback : DiffUtil.ItemCallback<FieldData>() {
    override fun areItemsTheSame(oldItem: FieldData, newItem: FieldData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FieldData, newItem: FieldData): Boolean {
        return oldItem == newItem
    }
}
