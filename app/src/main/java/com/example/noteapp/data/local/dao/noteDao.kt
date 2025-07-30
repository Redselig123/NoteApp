package com.example.noteapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteapp.data.local.entities.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao{
    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note:Note)

    @Query("SELECT * FROM Notes")
    fun getAllNotes(): Flow<List<Note>>//flow is native for corrutines, it use flow in corrutine by default

    @Query("DELETE FROM Notes Where id = :noteId")
    suspend fun deleteNote(noteId: Int)
}