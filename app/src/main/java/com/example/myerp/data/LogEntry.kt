package com.example.myerp.data

data class LogEntry(
    val fieldName: String,
    val amount: Double,
    val oldBalance: Double,
    val newBalance: Double,
    val comment: String? = null, // Επιλογή για σχόλια
    val timestamp: Long = System.currentTimeMillis() // Χρονική σήμανση
)