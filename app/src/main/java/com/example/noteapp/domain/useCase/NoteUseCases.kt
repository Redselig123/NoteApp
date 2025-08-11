package com.example.noteapp.domain.useCase

data class NoteUseCases(
    val getAllNotes: GetAllNotes,
    val getNoteByName: GetNoteByName,
    val insertNote: InsertNote,
    val deleteNote: DeleteNote,
    val shareNote: ShareNote,
    val exportNote: ExportNote
)
