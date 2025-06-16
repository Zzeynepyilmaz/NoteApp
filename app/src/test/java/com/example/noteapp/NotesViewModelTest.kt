package com.example.noteapp

import com.example.noteapp.data.repository.NoteRepository
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.notes.NotesViewModel
import com.example.noteapp.util.DataStoreManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: NotesViewModel
    private lateinit var repository: NoteRepository
    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        dataStoreManager = mockk(relaxed = true)
        viewModel = NotesViewModel(repository, dataStoreManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addNote() should call repository addNote and save last note`() = runTest {
        val noteContent = "Test notum"
        val note = Note(content = noteContent)

        viewModel.addNote(noteContent)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { repository.addNote(note) }
        coVerify { dataStoreManager.saveLastNote(noteContent) } // ✅ burası eklendi
    }
}
