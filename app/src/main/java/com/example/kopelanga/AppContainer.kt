package com.example.kopelanga

import android.content.Context
import com.example.kopelanga.data.NoteDatabase
import com.example.kopelanga.data.NotesRepository
import com.example.kopelanga.viewmodel.ViewModelFactory

class AppContainer(context: Context) {
    private val database by lazy { NoteDatabase.getDatabase(context) }
    private val repository by lazy { NotesRepository(database.noteDao()) }
    val viewModelFactory by lazy { ViewModelFactory(repository) }
}