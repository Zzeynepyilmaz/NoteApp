package com.example.noteapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.domain.model.Note
import com.example.noteapp.util.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    val lastNote = dataStoreManager.lastNote
    private var recentlyDeletedNote: Note? = null

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            repository.getAllNotes().collectLatest {
                _notes.value = it
            }
        }
    }

    fun addNote(content: String) {
        viewModelScope.launch {
            repository.addNote(Note(content = content))
            dataStoreManager.saveLastNote(content) // 💾 Kaydediyoruz
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            recentlyDeletedNote = note
        }
    }

    fun restoreNote() {
        recentlyDeletedNote?.let { note ->
            viewModelScope.launch {
                repository.addNote(note)
                recentlyDeletedNote = null
            }
        }
    }
}
