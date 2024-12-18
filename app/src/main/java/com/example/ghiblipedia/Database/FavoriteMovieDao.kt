package com.example.ghiblipedia.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movies WHERE userId = :userId")
    fun getFavorites(userId: String): LiveData<List<FavoriteMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(movie: FavoriteMovie)

    @Delete
    suspend fun removeFromFavorites(movie: FavoriteMovie)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieId = :movieId AND userId = :userId)")
    fun isFavorite(movieId: String, userId: String): LiveData<Boolean>
} 