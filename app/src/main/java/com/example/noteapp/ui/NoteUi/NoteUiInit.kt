package com.example.noteapp.ui.NoteUi

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.R
import com.example.noteapp.data.local.entities.Note
import com.example.noteapp.presentation.noteList.NoteListViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

enum class NoteScreen(@StringRes val title: Int) {
    Start(title = R.string.note_list),
    NoteInfo(title = R.string.note_info),
    New(title = R.string.new_note)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteAppBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SaveNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var noteTitle by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(R.string.dialog_save)) },
        text = {
            Column {
                Text(stringResource(R.string.save_note_dialog_sub_title))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    placeholder = { Text(stringResource(R.string.save_note_dialog_placeholder)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (noteTitle.isNotBlank()) {
                        onSave(noteTitle)
                    }
                }
            ) {
                Text(stringResource(R.string.dialog_save))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.dialog_cancel))
            }
        }
    )
}

@Composable
fun NewNote(
    viewModel: NoteListViewModel = hiltViewModel(),
    back: () -> Unit
) {
    var text by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var showUnsavedDialog by remember { mutableStateOf(false) }
    var pendingTitle by remember { mutableStateOf("") }
    var showOverwriteDialog by remember { mutableStateOf(false) }

    val hasChanges = text != ""

    // Interceptamos el back press físico
    BackHandler(enabled = hasChanges) {
        showUnsavedDialog = true
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (text.isNotBlank()) {
                        showDialog = true
                    }
                },
                modifier = Modifier.imePadding()//to not be under keyboard
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.floatting_action_button_content)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
                .imePadding(),//to not be under keyboard
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                modifier = Modifier.fillMaxSize(),
                placeholder = { Text(stringResource(R.string.button_placeholder)) },
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Send
                )
            )
        }
        if (showDialog) {
            SaveNoteDialog(
                onDismiss = { showDialog = false },
                onSave = { title ->
                    viewModel.viewModelScope.launch {
                        val exists = viewModel.doesNoteExist(title)
                        if (exists) {
                            pendingTitle = title
                            showDialog = false
                            showOverwriteDialog = true
                        } else {
                            viewModel.insertNote(content = text, name = title, id = null)
                            showDialog = false
                            back()
                        }
                    }
                }
            )
        }
        if (showOverwriteDialog) {
            AlertDialog(
                onDismissRequest = { showOverwriteDialog = false },
                title = { Text("La nota ya existe") },
                text = { Text("¿Querés sobrescribir la nota '$pendingTitle'?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.insertNote(
                            content = text,
                            name = pendingTitle,
                            id = null
                        )
                        showOverwriteDialog = false
                        back()
                    }) {
                        Text("Sobrescribir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showOverwriteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showUnsavedDialog) {
            AlertDialog(
                onDismissRequest = { showUnsavedDialog = false },
                title = { Text(stringResource(R.string.not_save_note_dialog_title)) },
                text = { Text(stringResource(R.string.not_save_note_dialog_subtitle)) },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = true
                        showUnsavedDialog = false
                    }) {
                        Text(stringResource(R.string.dialog_save))
                    }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = {
                            showUnsavedDialog = false
                            back()
                        }) {
                            Text(stringResource(R.string.dialog_dismis))
                        }
                        TextButton(onClick = { showUnsavedDialog = false }) {
                            Text(stringResource(R.string.dialog_cancel))
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun NoteList(
    noteListViewModel: NoteListViewModel,
    modifier: Modifier,
    onNoteClick: (Note) -> Unit,
    onButtonClick: () -> Unit
) {
    val notes by noteListViewModel.notes.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onButtonClick) {
                Icon(
                    painter = painterResource(R.drawable.pen_icon),
                    contentDescription = stringResource(R.string.note_button)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteItemList(
                    note = note,
                    onCardClick = { onNoteClick(note) },
                    onDelete = { noteToDelete ->
                        noteListViewModel.deleteNote(noteToDelete)
                    }
                )
            }
        }
    }
}

@Composable
fun NoteInfo(
    note: Note,
    onSave: (Note) -> Unit,
    onNoteSaved: () -> Unit
) {
    var content by remember { mutableStateOf(note.content) }
    var showUnsavedDialog by remember { mutableStateOf(false) }

    val hasChanges = content != (note?.content ?: "")

    // Interceptamos el back press físico
    BackHandler(enabled = hasChanges) {
        showUnsavedDialog = true
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (content != note.content) {
                        val updatedNote = note.copy(content = content)
                        onSave(updatedNote)
                    }
                    onNoteSaved()
                },
                modifier = Modifier.imePadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.note_info_button_description)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.doc_icon), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = note.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Editable: solo el contenido
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxSize(),
                textStyle = TextStyle(fontSize = 16.sp),
                placeholder = { Text(stringResource(R.string.button_placeholder)) },
                singleLine = false,
                maxLines = Int.MAX_VALUE
            )
        }
        if (showUnsavedDialog) {
            AlertDialog(
                onDismissRequest = { showUnsavedDialog = false },
                title = { Text(stringResource(R.string.not_save_note_dialog_title)) },
                text = { Text(stringResource(R.string.not_save_note_dialog_subtitle)) },
                confirmButton = {
                    TextButton(onClick = {
                        val noteToSave = note?.copy(content = content) ?: Note(
                            id = null,
                            content = content,
                            name = "Nueva nota"
                        )
                        onSave(noteToSave)
                        showUnsavedDialog = false
                        onNoteSaved()
                    }) {
                        Text(stringResource(R.string.dialog_save))
                    }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = {
                            showUnsavedDialog = false
                            onNoteSaved()
                        }) {
                            Text(stringResource(R.string.dialog_dismis))
                        }
                        TextButton(onClick = { showUnsavedDialog = false }) {
                            Text(stringResource(R.string.dialog_cancel))
                        }
                    }
                }
            )
        }
    }

}


@Composable
fun NoteItemList(note: Note, onCardClick: (note: Note) -> Unit, onDelete: (Note) -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCardClick(note)
            }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = note.name, fontSize = 18.sp, modifier = Modifier.weight(1f))
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.note_Item_list_button_description),
                    tint = Color.Red
                )
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.dialog_delete_title)) },
            text = { Text(stringResource(R.string.dialog_delete_sub_title)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(note)
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.dialog_delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
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
                arguments = listOf(navArgument("noteId") {
                    type = NavType.IntType
                })//listOf bc could e more arguments
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId")
                    ?: return@composable//recover argument or dont go to composable
                val viewModel: NoteListViewModel = hiltViewModel()
                val notes by viewModel.notes.collectAsState()

                // find note to show on screen
                val note = notes.firstOrNull { it.id == noteId }
                note?.let {
                    NoteInfo(
                        note = it,
                        onSave = { updatedNote ->
                            viewModel.insertNote(
                                content = updatedNote.content,
                                name = updatedNote.name,
                                id = updatedNote.id
                            )
                        },
                        onNoteSaved = { navController.popBackStack() }
                    )
                }
            }
            composable(route = NoteScreen.New.name) {
                NewNote(
                    back = { navController.popBackStack() }
                )
            }
        }
    }

}