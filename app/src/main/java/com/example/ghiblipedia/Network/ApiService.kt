package com.example.ghiblipedia.Network

import com.example.ghiblipedia.Model.Movie.Movie
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.ghiblipedia.Model.User.User

interface ApiService {
    @GET("user")
    fun getAllUsers(): Call<List<User>>

    @POST("user")
    fun createUser(@Body user: User): Call<User>
    
    @POST("movie")
    fun createMovie(@Body movie: Movie): Call<Movie>

    @GET("movie")
    fun getAllMovies(): Call<List<Movie>>
    
    @DELETE("movie/{id}")
    fun deleteMovie(@Path("id") id: String): Call<Void>
}