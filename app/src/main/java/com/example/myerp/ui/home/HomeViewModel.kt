package com.example.myerp.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myerp.data.AppDatabase
import com.example.myerp.data.FieldData
import kotlinx.coroutines.launch

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

    fun adjustFieldValue(field: FieldData, value: Double) {
        viewModelScope.launch {
            val updatedField = field.copy(total = field.total + value)
            fieldDao.insertField(updatedField) // Replace the existing field with the updated one
        }
    }
}