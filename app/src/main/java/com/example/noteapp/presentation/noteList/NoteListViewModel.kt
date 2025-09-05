package com.example.noteapp.presentation.noteList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.domain.useCase.DeleteNote
import com.example.noteapp.domain.useCase.GetAllNotes
import com.example.noteapp.domain.useCase.GetNoteByName
import com.example.noteapp.domain.useCase.InsertNote
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getAllNotes: GetAllNotes,
    private val insert: InsertNote,
    private val delete: DeleteNote,
    private val search: GetNoteByName
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _searchedNote = MutableStateFlow<Note?>(null)
    val searchedNote: StateFlow<Note?> = _searchedNote

    init {
        viewModelScope.launch {
            getAllNotes.get().collect { noteList ->
                _notes.value = noteList
            }
        }
    }

    public fun renameNote(note: Note, newName: String) {
        val updateNote = note.copy(name = newName)
        viewModelScope.launch {
            insert.get(updateNote)
        }
    }

    public fun insertNote(content: String, name: String, id: Int?) {
        val note = Note(id = id, content, name)
        viewModelScope.launch {
            insert.get(note)
        }

    }

    public fun deleteNote(note: Note) {
        viewModelScope.launch {
            val success = delete.delete(note)
            if (success == true) {
                //TODO MSJ
            }
        }
    }

    suspend fun doesNoteExist(title: String): Boolean {
        val note = search.get(title)
        return note != null
    }

    public fun getNoteByName(name: String) {
        viewModelScope.launch {
            val noteFind = search.get(name)
            _searchedNote.value = noteFind
        }
    }
}