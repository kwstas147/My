package com.example.myerp.ui.gallery

import androidx.lifecycle.*
import com.example.myerp.data.FieldDao
import com.example.myerp.data.LogEntry
import com.example.myerp.data.LogEntryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(
    private val logEntryDao: LogEntryDao,
    private val fieldDao: FieldDao
) : ViewModel() {

    private val _logEntries = MutableLiveData<List<LogEntry>>()
    val logEntries: LiveData<List<LogEntry>> get() = _logEntries

    private val _fields = MutableLiveData<List<String>>() // Example for fields
    val fields: LiveData<List<String>> get() = _fields

    fun loadLogEntries() {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                logEntryDao.getAllLogEntriesList()
            }
            _logEntries.value = entries
        }
    }

    fun loadFields() {
        viewModelScope.launch {
            val fieldList = withContext(Dispatchers.IO) {
                fieldDao.getAllFieldsList().map { it.name }
            }
            _fields.value = fieldList
        }
    }

    fun addLogEntry(newEntry: LogEntry) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val entryWithDefaultComment = newEntry.copy(comment = newEntry.comment.ifEmpty { "" })
                logEntryDao.insertLogEntry(entryWithDefaultComment)
            }
            loadLogEntries()
        }
    }


    fun updateLogEntryComment(logEntry: LogEntry, comment: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val updatedEntry = logEntry.copy(comment = comment)
                logEntryDao.insertLogEntry(updatedEntry) // Update the entry in the database
            }
            loadLogEntries() // Refresh the list
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

    fun filterLogEntries(query: String) {
        viewModelScope.launch {
            val filteredEntries = withContext(Dispatchers.IO) {
                logEntryDao.getFilteredLogEntries("%$query%")
            }
            _logEntries.value = filteredEntries
        }
    }

    fun shareLogEntries(logEntries: List<LogEntry>): String {
        return logEntries.joinToString("\n") { entry ->
            "Field: ${entry.fieldName}, Amount: ${entry.amount}, Old Balance: ${entry.oldBalance}, " +
            "New Balance: ${entry.newBalance}, Comment: ${entry.comment}, Timestamp: ${entry.timestamp}"
        }
    }

    companion object {
        fun provideFactory(
            logEntryDao: LogEntryDao,
            fieldDao: FieldDao
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GalleryViewModel(logEntryDao, fieldDao) as T
            }
        }
    }
}
