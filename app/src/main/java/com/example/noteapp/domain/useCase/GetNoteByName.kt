package com.example.noteapp.domain.useCase

import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByName @Inject constructor(private val repository: NoteRepository) {
    suspend fun get(name: String): Note? {
        return repository.getNoteByName(name)
    }
}