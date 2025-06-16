package com.example.noteapp.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "note_prefs")

class DataStoreManager(private val context: Context) {

    companion object {
        val LAST_NOTE_KEY = stringPreferencesKey("last_note")
    }

    val lastNote: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LAST_NOTE_KEY]
    }

    suspend fun saveLastNote(content: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_NOTE_KEY] = content
        }
    }
}