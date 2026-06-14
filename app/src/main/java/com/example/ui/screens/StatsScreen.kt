package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MelodyViewModel

@Composable
fun StatsScreen(viewModel: MelodyViewModel) {
    val mostPlayed by viewModel.mostPlayedSongs.collectAsStateWithLifecycle()
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderSection("Most Played")
        
        if (mostPlayed.isEmpty()) {
            EmptyState(
                message = "Start listening to see stats",
                icon = Icons.Default.BarChart
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).testTag("stats_list"),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(mostPlayed) { index, song ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            SongItem(
                                song = song,
                                isCurrent = song.id == currentSong?.id,
                                onSelect = { viewModel.selectSong(song) },
                                onToggleFavorite = { viewModel.toggleFavorite(song) }
                            )
                        }
                        Text(
                            text = "${song.playCount} plays",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(end = 16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
