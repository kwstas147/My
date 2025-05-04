package com.example.myerp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LogEntryDao {
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC")
    suspend fun getAllLogEntriesList(): List<LogEntry>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogEntry(logEntry: LogEntry)

    @Delete
    suspend fun deleteLogEntry(logEntry: LogEntry)

    @Query("DELETE FROM log_entries")
    suspend fun deleteAllLogEntries()

    @Query("SELECT * FROM log_entries WHERE timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getLogEntriesByDateRange(startDate: Long, endDate: Long): LiveData<List<LogEntry>>

    @Query("SELECT * FROM log_entries WHERE fieldName LIKE :fieldName ORDER BY timestamp DESC")
    fun getLogEntriesByFieldName(fieldName: String): LiveData<List<LogEntry>>

    @Query("SELECT * FROM log_entries WHERE amount BETWEEN :minAmount AND :maxAmount ORDER BY timestamp DESC")
    fun getLogEntriesByAmountRange(minAmount: Double, maxAmount: Double): LiveData<List<LogEntry>>

    @Query("SELECT COUNT(*) FROM log_entries")
    suspend fun getLogEntryCount(): Int

    @Query("DELETE FROM log_entries WHERE fieldName = :fieldName")
    suspend fun deleteLogEntriesByFieldName(fieldName: String)


}