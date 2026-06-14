package com.example

import android.app.Application
import com.example.data.MelodyDatabase
import com.example.data.SongRepository

class MelodyApplication : Application() {
    val database: MelodyDatabase by lazy { MelodyDatabase.getDatabase(this) }
    val repository: SongRepository by lazy { SongRepository(database.songDao()) }
}
