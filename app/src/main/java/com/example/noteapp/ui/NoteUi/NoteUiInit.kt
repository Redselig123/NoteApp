package com.example.noteapp.ui.NoteUi

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.noteapp.R
import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.presentation.noteList.NoteListViewModel


@Composable
fun NoteItem(note: Note) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable {
    }) {
        Row() {
            Icon(painterResource(R.drawable.doc_icon), contentDescription = null)
            Text(text = note.name, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun NoteList(noteListViewModel: NoteListViewModel, modifier: Modifier){
    Log.d("NOTELIST", "HOLA")
    val notes by noteListViewModel.notes.collectAsState()
    LazyColumn {
        items(notes){ note ->
            NoteItem(
                note = note
            )
        }
    }
}

@Composable
fun noteScreen(){
    Text("NOTA INDIVIDUAL", textAlign = TextAlign.Center)
}