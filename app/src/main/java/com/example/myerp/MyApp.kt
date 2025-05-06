package com.example.myerp

import android.app.Application
import com.example.myerp.data.AppDatabase

class MyApp : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}