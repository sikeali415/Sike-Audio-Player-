package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val artist: String,
    val album: String = "Unknown Album",
    val durationMs: Long = 0,
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val fileName: String = "", // Used to identify the local file/asset
    val lastPlayedTimestamp: Long = 0,
    val isOffline: Boolean = true // Songs are stored locally by default
)
