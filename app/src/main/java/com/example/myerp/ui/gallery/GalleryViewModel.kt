package com.example.myerp.ui.gallery

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.LogEntry
import com.example.myerp.data.LogEntryDao
import com.example.myerp.data.LogEntryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(application: Application) : ViewModel() {

    private val logEntryDao: LogEntryDao = LogEntryDatabase.getDatabase(application).logEntryDao()

    private val _logEntries = MutableLiveData<List<LogEntry>>()
    val logEntries: LiveData<List<LogEntry>> = _logEntries

    fun loadLogEntries() {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                logEntryDao.getAllLogEntries()
            }
            _logEntries.postValue(entries)
        }
    }

    fun filterLogEntries(filterText: String) {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                logEntryDao.getLogEntriesByFieldName(filterText)
            }
            _logEntries.postValue(entries)
        }
    }

    fun addLogEntry(newEntry: LogEntry) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logEntryDao.insertLogEntry(newEntry)
            }
            loadLogEntries()
        }
    }

    fun deleteLogEntry(logEntry: LogEntry) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logEntryDao.deleteLogEntry(logEntry)
            }
            loadLogEntries()
        }
    }

    fun deleteLogEntriesByFieldName(fieldName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logEntryDao.deleteLogEntriesByFieldName(fieldName)
            }
            loadLogEntries()
        }
    }

    fun shareLogEntries(logEntries: List<LogEntry>): String {
        // Convert log entries to a string for sharing
        return logEntries.joinToString("\n") { entry ->
            "Field Name: ${entry.fieldName}, Amount: ${entry.amount}, Old Balance: ${entry.oldBalance}, New Balance: ${entry.newBalance}, Comment: ${entry.comment}"
        }
    }
}