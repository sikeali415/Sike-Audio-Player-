package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Favorites

@Serializable
object Stats

@Serializable
object About

@Serializable
data class ArtistDetail(val artistName: String)
