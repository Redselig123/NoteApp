package com.example.noteapp.ui.note

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.presentation.noteList.NoteListViewModel

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