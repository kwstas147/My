package com.example.myerp.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myerp.data.FieldDao
import com.example.myerp.data.LogEntry
import com.example.myerp.data.LogEntryDao
import com.example.myerp.data.FieldData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(private val logEntryDao: LogEntryDao, private val fieldDao: FieldDao) : ViewModel() {

    private val _logEntries = MutableLiveData<List<LogEntry>>()
    val logEntries: LiveData<List<LogEntry>> = _logEntries

    private val _fields = MutableLiveData<List<FieldData>>()
    val fields: LiveData<List<FieldData>> = _fields

    fun loadLogEntries() {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                logEntryDao.getAllLogEntriesList()
            }
            println("Entries loaded from database: $entries") // Debugging line
            if (entries.isEmpty()) {
                println("No log entries found in the database.") // Debugging line
            }
            _logEntries.postValue(entries)
        }
    }

    fun filterLogEntries(filterText: String) {
        viewModelScope.launch {
            val filteredEntries = withContext(Dispatchers.IO) {
                logEntryDao.getFilteredLogEntries("%$filterText%")
            }
            _logEntries.postValue(filteredEntries)
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

    fun loadFields() {
        viewModelScope.launch {
            val fieldList: List<FieldData> = withContext(Dispatchers.IO) {
                fieldDao.getAllFieldsList()
            }
            println("Fields loaded from database: $fieldList") // Debugging line
            _fields.postValue(fieldList)
        }
    }
    companion object {
        fun provideFactory(logEntryDao: LogEntryDao, fieldDao: FieldDao): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
                        return GalleryViewModel(logEntryDao, fieldDao) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
