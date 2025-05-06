package com.example.myerp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_entries")
data class LogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "fieldName")
    val fieldName: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "oldBalance")
    val oldBalance: Double,

    @ColumnInfo(name = "newBalance")
    val newBalance: Double,

    @ColumnInfo(name = "comment")
    val comment: String = "", // Default to empty string

    @ColumnInfo(name = "timestamp")
    val timestamp: Long

)
