package com.example.noteapp.presentation.noteList

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
        // simular notas
        viewModelScope.launch {

        }
    }

    public fun insertNote(content: String, name: String) {
        val nota = Note(id = null, content, name)
        viewModelScope.launch {
            insert.get(nota)
        }//TODO validate

    }

    public fun deleteNote(note: Note) {
        viewModelScope.launch {
            val success = delete.delete(note)
            if (success == true) {
                //TODO MSJ
            }
        }
    }

    public fun getNoteByName(name: String) {
        viewModelScope.launch {
            val noteFind = search.get(name)
            _searchedNote.value = noteFind
        }

    }
}