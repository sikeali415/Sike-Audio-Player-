package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MelodyViewModel

@Composable
fun FavoritesScreen(viewModel: MelodyViewModel) {
    val favorites by viewModel.favoriteSongs.collectAsStateWithLifecycle()
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection("Favorites")
        
        if (favorites.isEmpty()) {
            EmptyState(
                message = "No favorites yet",
                icon = Icons.Default.Favorite
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).testTag("favorites_list"),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(favorites) { song ->
                    SongItem(
                        song = song,
                        isCurrent = song.id == currentSong?.id,
                        onSelect = { viewModel.selectSong(song) },
                        onToggleFavorite = { viewModel.toggleFavorite(song) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
