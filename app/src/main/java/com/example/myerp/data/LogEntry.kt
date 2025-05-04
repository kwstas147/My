package com.example.myerp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_entries")
data class LogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fieldName: String,
    val amount: Double,
    val oldBalance: Double,
    val newBalance: Double,
    val comment: String?,
    val timestamp: Long = System.currentTimeMillis()
)