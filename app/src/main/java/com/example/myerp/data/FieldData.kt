package com.example.myerp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fields")
data class FieldData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    var total: Double
)