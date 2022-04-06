package com.thaariq.notesapp.data

import androidx.lifecycle.LiveData
import com.thaariq.notesapp.data.entity.Notes
import com.thaariq.notesapp.data.room.NotesDao

class NotesRepository(private val notesDao : NotesDao) {

    fun getAllData(): LiveData<List<Notes>> = notesDao.getAllData()

    // fungsi ini untuk insert/ADD kedalam database
    suspend fun insertNotes(notes:Notes){
        notesDao.insertNotes(notes)
    }

    fun sortByHighPriority() : LiveData<List<Notes>> = notesDao.sortByHighPriority()
    fun sortByLowPriority() : LiveData<List<Notes>> = notesDao.sortByLowPriority()

    suspend fun deleteAllData() = notesDao.deleteAllData()

    fun searchByQuery(query : String) : LiveData<List<Notes>> =  notesDao.searchByQuery(query)

    suspend fun deleteNote(notes: Notes){
        notesDao.deleteNote(notes)
    }

    suspend fun updateNotes(notes: Notes){
        notesDao.updateNotes(notes)
    }


}