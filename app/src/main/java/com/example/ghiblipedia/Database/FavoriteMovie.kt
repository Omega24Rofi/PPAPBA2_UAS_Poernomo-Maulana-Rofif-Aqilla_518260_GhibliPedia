package com.example.ghiblipedia.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovie(
    @PrimaryKey val movieId: String,
    val title: String,
    val director: String,
    val releasedate: String,
    val synopis: String,
    val imagelink: String,
    val userId: String  // to associate favorites with specific users
) 