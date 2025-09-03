package com.example.noteapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Notes",
    indices = [Index(value = ["name"], unique = true)]
)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val content: String,
    @ColumnInfo(name = "name")
    val name: String
)
