package com.thaariq.notesapp.data.room

import androidx.room.TypeConverter
import com.thaariq.notesapp.data.entity.Priority

class Converter {

    // untuk convert dari Priority ke String
    // fungsi ini digunakan ketika GET sebuah database
    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }

    // untuk meng-convert sebuah string menjadi enum Priority
    // fungsi ini digunakan ketika ADD dan UPDATE sebuah data kedalam database
    @TypeConverter
    fun toPriority(priority : String): Priority{
        return Priority.valueOf(priority)
    }
}