package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class MelodyDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var Instance: MelodyDatabase? = null

        fun getDatabase(context: Context): MelodyDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MelodyDatabase::class.java, "melody_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
