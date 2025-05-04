package com.example.myerp.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myerp.data.LogEntry // Υποθέτουμε ότι αυτό είναι το μοντέλο δεδομένων σου

class GalleryViewModel : ViewModel() {

    private val _logEntries = MutableLiveData<List<LogEntry>>(emptyList())
    val logEntries: LiveData<List<LogEntry>> = _logEntries

    // Προσθήκη νέας καταγραφής
    fun addLogEntry(entry: LogEntry) {
        val currentEntries = _logEntries.value.orEmpty().toMutableList()
        currentEntries.add(entry)
        _logEntries.value = currentEntries
        // Εδώ θα προσθέσεις κώδικα για την αποθήκευση στην Room
    }

    // Φόρτωση καταγραφών (από τη Room)
    fun loadLogEntries() {
        // Εδώ θα προσθέσεις κώδικα για την ανάκτηση από τη Room
        // ...
        // _logEntries.value = entriesFromRoom
    }

    // Φιλτράρισμα καταγραφών
    fun filterLogEntries(filter: String) {
        //Εδώ υλοποιείται το φιλτράρισμα.
    }

    fun shareLogEntries(entries: List<LogEntry>) {
        //Εδώ υλοποιείται η κοινοποίηση.
    }

    // Άλλες μέθοδοι για διαγραφή, τροποποίηση, κλπ.
}