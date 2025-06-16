package com.example.noteapp.presentation.notes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState()
    var newNote by remember { mutableStateOf("") }
    val lastNote by viewModel.lastNote.collectAsState(initial = "")

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            if (!lastNote.isNullOrBlank()) {
                Text(text = "Son Not: $lastNote", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = newNote,
                onValueChange = { newNote = it },
                label = { Text("Yeni not gir") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (newNote.isNotBlank()) {
                        viewModel.addNote(newNote)
                        newNote = ""
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Ekle")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(notes, key = { it.id }) { note ->
                    NoteItem(
                        note = note,
                        onDelete = {
                            viewModel.deleteNote(note)
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Not silindi",
                                    actionLabel = "Geri Al"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.restoreNote()
                                }
                            }
                        },
                        modifier = Modifier.animateItemPlacement() // 💫 animasyon burada
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = note.content)
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Sil")
            }
        }
    }
}
