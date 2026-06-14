package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.MelodyApplication
import com.example.data.Song
import com.example.data.SongRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MelodyViewModel(private val repository: SongRepository) : ViewModel() {

    val allSongs: StateFlow<List<Song>> = repository.getAllSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteSongs: StateFlow<List<Song>> = repository.getFavoriteSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mostPlayedSongs: StateFlow<List<Song>> = repository.getMostPlayedSongs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allArtists: StateFlow<List<String>> = repository.getAllArtists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    init {
        seedDatabase()
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            repository.getAllSongs().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList()).value.let {
                if (it.isEmpty()) {
                    val dummySongs = listOf(
                        Song(title = "Midnight City", artist = "M83", album = "Hurry Up, We're Dreaming", durationMs = 243000),
                        Song(title = "Starboy", artist = "The Weeknd", album = "Starboy", durationMs = 230000),
                        Song(title = "Blinding Lights", artist = "The Weeknd", album = "After Hours", durationMs = 200000),
                        Song(title = "Instant Crush", artist = "Daft Punk", album = "Random Access Memories", durationMs = 337000),
                        Song(title = "Get Lucky", artist = "Daft Punk", album = "Random Access Memories", durationMs = 248000),
                        Song(title = "One More Time", artist = "Daft Punk", album = "Discovery", durationMs = 320000),
                        Song(title = "Level of Concern", artist = "Twenty One Pilots", durationMs = 220000),
                        Song(title = "Stressed Out", artist = "Twenty One Pilots", durationMs = 202000)
                    )
                    repository.insertSongs(dummySongs)
                }
            }
        }
    }

    fun selectSong(song: Song) {
        _currentSong.value = song
        _isPlaying.value = true
        viewModelScope.launch {
            repository.incrementPlayCount(song.id)
        }
    }

    fun togglePlayback() {
        _isPlaying.value = !_isPlaying.value
    }

    fun toggleFavorite(song: Song) {
        viewModelScope.launch {
            repository.toggleFavorite(song.id, !song.isFavorite)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MelodyApplication)
                MelodyViewModel(application.repository)
            }
        }
    }
}
