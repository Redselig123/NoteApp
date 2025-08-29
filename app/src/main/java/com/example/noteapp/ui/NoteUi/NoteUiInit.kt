package com.example.noteapp.ui.NoteUi

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.R
import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.presentation.noteList.NoteListViewModel
import com.example.noteapp.ui.theme.Shapes

enum class NoteScreen(@StringRes val title: Int) {
    Start(title = R.string.note_list),
    NoteInfo(title = R.string.note_info),
    New(title = R.string.new_note)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAppBar() {
    TopAppBar(
        title = { Text("Note App") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NewNote() {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            modifier = Modifier.fillMaxSize(),
            placeholder = { Text("Escribe aquí...") },
            textStyle = TextStyle(fontSize = 18.sp),
            singleLine = false, // permite múltiples líneas
            maxLines = Int.MAX_VALUE // sin límite de líneas
        )
    }
}
@Composable
fun NoteList(
    noteListViewModel: NoteListViewModel,
    modifier: Modifier,
    onNoteClick: (Note) -> Unit,
    onButtonClick:() -> Unit
) {
    val notes by noteListViewModel.notes.collectAsState()
    OutlinedButton(
        onClick =  onButtonClick,
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp),
        shape = Shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors()
    ){
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(painterResource(R.drawable.pen_icon) , contentDescription = null)
            Text(stringResource(R.string.note_button), textAlign = TextAlign.Center)
        }
    }
    LazyColumn {
        items(notes) { note ->
            NoteItem(
                note = note,
                onCardClick = { onNoteClick(note) }
            )
        }
    }
}

@Composable
fun NoteInfo(note: Note) {
    Row() {
        Icon(painterResource(R.drawable.doc_icon), contentDescription = null)
        Text(text = note.name, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun NoteItem(note: Note, onCardClick: (note: Note) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCardClick(note)
            }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.name, fontSize = 18.sp)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {
            NoteAppBar()
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NoteScreen.Start.name,
            modifier = Modifier.padding(padding)
        ) {
            composable(route = NoteScreen.Start.name) {
                NoteList(
                    noteListViewModel = hiltViewModel(),
                    modifier = Modifier,
                    onNoteClick = { note ->
                        navController.navigate("${NoteScreen.NoteInfo.name}/${note.id}")
                    },
                    onButtonClick = {
                        navController.navigate(NoteScreen.New.name) // ejemplo: navegar a una pantalla en blanco
                    }
                )
            }
            composable(
                route = "${NoteScreen.NoteInfo.name}/{noteId}",//route with parameter
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })//listOf bc could e more arguments
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable//recover argument or dont go to composable
                val viewModel: NoteListViewModel = hiltViewModel()
                val notes by viewModel.notes.collectAsState()

                // find note to show on screen
                val note = notes.firstOrNull { it.id == noteId }
                note?.let {
                    NoteInfo(note = it)
                }
            }
            composable(route = NoteScreen.New.name){
                NewNote()
            }
        }
    }

}