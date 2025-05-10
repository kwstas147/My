package com.example.myerp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.AppDatabase
import com.example.myerp.data.FieldData
import com.example.myerp.data.LogEntry
import com.example.myerp.ui.slideshow.SlideshowViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val fieldDao = AppDatabase.getDatabase(application).fieldDao()
    val fieldData: LiveData<List<FieldData>> = fieldDao.getAllFields()

    fun addField(name: String) {
        viewModelScope.launch {
            fieldDao.insertField(FieldData(name = name, total = 0.0))
        }
    }

    fun removeField(field: FieldData) {
        viewModelScope.launch {
            fieldDao.deleteField(field)
        }
    }

    fun adjustFieldValue(field: FieldData, value: Double, slideshowViewModel: SlideshowViewModel) {
        viewModelScope.launch {
            val updatedField = field.copy(total = field.total + value)
            fieldDao.insertField(updatedField)

            // Αποθήκευση στο ημερολόγιο
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
            slideshowViewModel.saveFieldDataToCalendar(
                fieldName = field.name,
                amount = value,
                oldBalance = field.total,
                newBalance = updatedField.total,
                date = currentDate
            )

            // Δημιουργία και αποθήκευση LogEntry
            val logEntry = LogEntry(
                fieldName = field.name,
                amount = value,
                oldBalance = field.total,
                newBalance = updatedField.total,
                timestamp = System.currentTimeMillis()
            )
            AppDatabase.getDatabase(getApplication()).logEntryDao().insertLogEntry(logEntry)
        }
    }
}
