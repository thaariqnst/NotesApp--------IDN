package com.thaariq.notesapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.thaariq.notesapp.data.NotesRepository
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.data.room.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) :AndroidViewModel(application) {
    private val notesDao = NotesDatabase.getDatabase(application).notesDao()
    private val repository : NotesRepository = NotesRepository(notesDao)

    fun getAllData() : LiveData<List<Notes>> = repository.getAllData()

    // fungsi ini yang akan digunakan oleh view ketika input data dan dikirimkan kedalam repository
    fun insertData(notes : Notes){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNotes(notes)
        }
    }

    fun sortByHighPriority() : LiveData<List<Notes>> = repository.sortByHighPriority()
    fun sortByLowPriority() : LiveData<List<Notes>> = repository.sortByLowPriority()

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }

    fun searchByQuery(query : String) : LiveData<List<Notes>> = repository.searchByQuery(query)

    fun deleteNote(notes: Notes){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(notes)
        }
    }

    fun updateNote(notes: Notes){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNotes(notes)
        }
    }

}