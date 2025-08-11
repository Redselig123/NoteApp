package com.example.noteapp.domain.useCase

import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

class ShareNote @Inject constructor(private val repository:NoteRepository) {
    suspend fun get(note: Note) {
        return repository.shareNote(note)
    }
}
