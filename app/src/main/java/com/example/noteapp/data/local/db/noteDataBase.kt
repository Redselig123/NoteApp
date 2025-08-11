package com.example.noteapp.data.local.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapp.data.local.dao.NoteDao

abstract class noteDataBase : RoomDatabase(){
    abstract fun noteDao(): NoteDao

    companion object{//cannot be instanced. 1 for all instance of the class
        @Volatile private var INSTANCE: noteDataBase? = null//visible for all threads

        fun getNoteDataBase(context: Context): noteDataBase {
            return INSTANCE ?: synchronized(this){//just 1 thread create the db
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    noteDataBase::class.java, "note_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}