package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.local.NoteDatabase
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.presentation.notes.NotesScreen
import com.example.noteapp.presentation.notes.NotesViewModel
import com.example.noteapp.presentation.notes.NotesViewModelFactory
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.util.DataStoreManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dao = NoteDatabase.getDatabase(applicationContext).noteDao()
        val repository = NoteRepository(dao)
        val dataStoreManager = DataStoreManager(applicationContext)
        val viewModelFactory = NotesViewModelFactory(repository, dataStoreManager)
        val viewModel = ViewModelProvider(this, viewModelFactory)[NotesViewModel::class.java]
        setContent {
            NoteAppTheme {
                NotesScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NoteAppTheme {
        Greeting("Android")
    }
}