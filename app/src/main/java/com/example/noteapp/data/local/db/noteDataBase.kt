package com.example.noteapp.data.local.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noteapp.data.local.dao.NoteDao
import com.example.noteapp.data.local.entities.Note


@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = false,
)
abstract class noteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: noteDataBase? = null


        fun getNoteDataBase(context: Context): noteDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    noteDataBase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}