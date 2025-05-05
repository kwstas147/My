package com.example.myerp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FieldDao {

    @Query("SELECT * FROM fields ORDER BY name ASC")
    suspend fun getAllFieldsList(): List<FieldData>

    @Query("SELECT * FROM fields")
    fun getAllFields(): LiveData<List<FieldData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: FieldData)

    @Delete
    suspend fun deleteField(field: FieldData)
}