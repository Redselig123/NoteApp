package com.example.noteapp.domain.useCase


import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllNotes @Inject constructor(private val repository: NoteRepository) {
    fun get(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}