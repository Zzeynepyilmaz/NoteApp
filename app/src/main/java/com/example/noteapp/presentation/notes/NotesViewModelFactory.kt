package com.example.noteapp.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.util.DataStoreManager

class NotesViewModelFactory(
    private val repository: NoteRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotesViewModel(repository, dataStoreManager) as T
    }
}
