package com.example.myerp.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.AppDatabase
import com.example.myerp.data.LogEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    private val _logEntries = MutableLiveData<List<LogEntry>>(emptyList())
    val logEntries: LiveData<List<LogEntry>> = _logEntries

    // Add a new log entry
    fun addLogEntry(entry: LogEntry) {
        val currentEntries = _logEntries.value.orEmpty().toMutableList()
        currentEntries.add(entry)
        _logEntries.value = currentEntries
        saveLogEntryToDatabase(entry)
    }

    // Load log entries from Room database
    fun loadLogEntries() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val liveDataEntries = fetchLogEntriesFromDatabase()
                liveDataEntries.observeForever { entries ->
                    _logEntries.postValue(entries)
                }
            }
        }
    }

    // Filter log entries based on a search query
    fun filterLogEntries(filter: String) {
        val allEntries = _logEntries.value.orEmpty()
        val filteredEntries = allEntries.filter { entry ->
            entry.fieldName.contains(filter, ignoreCase = true) ||
                    entry.comment?.contains(filter, ignoreCase = true) == true ||
                    entry.timestamp.toString().contains(filter)
        }
        _logEntries.value = filteredEntries
    }

    fun getLogEntriesByDateRange(startDate: Long, endDate: Long) {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(getApplication<Application>()).logEntryDao()
                    .getLogEntriesByDateRange(startDate, endDate).value.orEmpty()
            }
            _logEntries.postValue(entries)
        }
    }

    fun getLogEntriesByFieldName(fieldName: String) {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(getApplication<Application>()).logEntryDao()
                    .getLogEntriesByFieldName(fieldName).value.orEmpty()
            }
            _logEntries.postValue(entries)
        }
    }

    fun getLogEntriesByAmountRange(minAmount: Double, maxAmount: Double) {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(getApplication<Application>()).logEntryDao()
                    .getLogEntriesByAmountRange(minAmount, maxAmount).value.orEmpty()
            }
            _logEntries.postValue(entries)
        }
    }
    fun getLogEntryCount(onResult: (Int) -> Unit) {
        viewModelScope.launch {
            val count = withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(getApplication<Application>()).logEntryDao().getLogEntryCount()
            }
            onResult(count)
        }
    }
    fun shareLogEntries(logEntries: List<LogEntry>): String {
        return logEntries.joinToString(separator = "\n") { entry ->
            """
        Field Name: ${entry.fieldName}
        Amount: ${entry.amount}
        Old Balance: ${entry.oldBalance}
        New Balance: ${entry.newBalance}
        Comment: ${entry.comment ?: "No comment"}
        Timestamp: ${entry.timestamp}
        """.trimIndent()
        }
    }

    fun deleteLogEntriesByFieldName(fieldName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(getApplication<Application>()).logEntryDao()
                .deleteLogEntriesByFieldName(fieldName)
            loadLogEntries()
        }
    }

    fun saveLogEntryToDatabase(entry: LogEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(getApplication<Application>()).logEntryDao().insertLogEntry(entry)
        }
    }

    private fun fetchLogEntriesFromDatabase(): LiveData<List<LogEntry>> {
        return database.logEntryDao().getAllLogEntries()
    }
    fun deleteLogEntry(logEntry: LogEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            database.logEntryDao().deleteLogEntry(logEntry)
            loadLogEntries()
        }
    }
}