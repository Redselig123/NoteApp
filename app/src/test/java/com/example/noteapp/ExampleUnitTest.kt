package com.example.noteapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import android.content.Context

import com.example.noteapp.data.local.db.noteDataBase
import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.data.repository.NoteRepositoryImpl
import com.example.noteapp.domain.useCase.DeleteNote
import com.example.noteapp.domain.useCase.GetAllNotes
import com.example.noteapp.domain.useCase.GetNoteByName
import com.example.noteapp.domain.useCase.InsertNote
import com.example.noteapp.presentation.noteList.NoteListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


