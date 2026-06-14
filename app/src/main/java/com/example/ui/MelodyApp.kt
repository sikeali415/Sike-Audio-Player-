package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.Song
import com.example.ui.navigation.*
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StatsScreen

@Composable
fun MelodyApp(
    viewModel: MelodyViewModel = viewModel(factory = MelodyViewModel.Factory)
) {
    val navController = rememberNavController()
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = currentSong != null,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    currentSong?.let { song ->
                        NowPlayingBar(
                            song = song,
                            isPlaying = isPlaying,
                            onTogglePlayback = { viewModel.togglePlayback() },
                            onClick = { /* Could open detail screen */ }
                        )
                    }
                }
                MelodyBottomBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Home> {
                HomeScreen(
                    viewModel = viewModel,
                    onAboutClick = { navController.navigate(About) }
                )
            }
            composable<Favorites> {
                FavoritesScreen(viewModel = viewModel)
            }
            composable<Stats> {
                StatsScreen(viewModel = viewModel)
            }
            composable<About> {
                AboutScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun MelodyBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(if (currentDestination?.hierarchy?.any { it.hasRoute<Home>() } == true) Icons.Filled.MusicNote else Icons.Outlined.MusicNote, contentDescription = "Library") },
            label = { Text("Library") },
            selected = currentDestination?.hierarchy?.any { it.hasRoute<Home>() } == true,
            onClick = {
                navController.navigate(Home) {
                    popUpTo(Home) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(if (currentDestination?.hierarchy?.any { it.hasRoute<Favorites>() } == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = currentDestination?.hierarchy?.any { it.hasRoute<Favorites>() } == true,
            onClick = {
                navController.navigate(Favorites) {
                    popUpTo(Home) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(if (currentDestination?.hierarchy?.any { it.hasRoute<Stats>() } == true) Icons.Filled.BarChart else Icons.Outlined.BarChart, contentDescription = "Stats") },
            label = { Text("Stats") },
            selected = currentDestination?.hierarchy?.any { it.hasRoute<Stats>() } == true,
            onClick = {
                navController.navigate(Stats) {
                    popUpTo(Home) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun NowPlayingBar(
    song: Song,
    isPlaying: Boolean,
    onTogglePlayback: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fake cover art
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onTogglePlayback) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }
        }
    }
}
