package com.example.myerp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LogEntry::class], version = 1, exportSchema = false)
abstract class LogEntryDatabase : RoomDatabase() {

    abstract fun logEntryDao(): LogEntryDao

    companion object {
        @Volatile
        private var INSTANCE: LogEntryDatabase? = null

        fun getDatabase(context: Context): LogEntryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LogEntryDatabase::class.java,
                    "log_entry_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}