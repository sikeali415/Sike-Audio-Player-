package com.example.data

import kotlinx.coroutines.flow.Flow

class SongRepository(private val songDao: SongDao) {
    fun getAllSongs(): Flow<List<Song>> = songDao.getAllSongs()
    fun getFavoriteSongs(): Flow<List<Song>> = songDao.getFavoriteSongs()
    fun getMostPlayedSongs(): Flow<List<Song>> = songDao.getMostPlayedSongs()
    fun getSongsByArtist(artist: String): Flow<List<Song>> = songDao.getSongsByArtist(artist)
    fun getAllArtists(): Flow<List<String>> = songDao.getAllArtists()

    suspend fun insertSongs(songs: List<Song>) = songDao.insertSongs(songs)
    suspend fun updateSong(song: Song) = songDao.updateSong(song)
    suspend fun incrementPlayCount(songId: Int) = songDao.incrementPlayCount(songId)
    suspend fun toggleFavorite(songId: Int, isFavorite: Boolean) = songDao.setFavorite(songId, isFavorite)
}
