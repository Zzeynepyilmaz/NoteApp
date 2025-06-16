package com.example.noteapp.data.repository

import com.example.noteapp.data.local.NoteDao
import com.example.noteapp.data.local.NoteEntity
import com.example.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(private val dao: NoteDao) {

    suspend fun addNote(note: Note) {
        dao.insertNote(NoteEntity(content = note.content))
    }

    fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { list ->
            list.map { entity ->
                Note(id = entity.id, content = entity.content)
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        dao.deleteNote(NoteEntity(id = note.id, content = note.content))
    }
}