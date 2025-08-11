package com.example.noteapp.domain.useCase

import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNote @Inject constructor(private val repository: NoteRepository) {
    suspend fun delete(note: Note): Boolean {
       return repository.deleteNote(note)
    }
}