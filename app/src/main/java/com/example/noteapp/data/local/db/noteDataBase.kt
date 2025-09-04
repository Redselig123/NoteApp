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
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `Notes_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `content` TEXT NOT NULL,
                `name` TEXT NOT NULL
            )
        """
                )
                database.execSQL(
                    """
            INSERT INTO Notes_new (id, content, name)
            SELECT id, content, name FROM Notes
        """
                )
                // must not have been duplicated already, otherwise will fail
                database.execSQL("DROP TABLE Notes")
                database.execSQL("ALTER TABLE Notes_new RENAME TO Notes")
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Notes_name` ON `Notes` (`name`)")
            }
        }


        fun getNoteDataBase(context: Context): noteDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    noteDataBase::class.java,
                    "note_database"
                ).addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}