package com.example.ghiblipedia.Model.Movie

import com.google.gson.annotations.SerializedName

class Movie (
    @SerializedName("_id")
    val id: String? ,
    @SerializedName("title")
    val title: String,
    @SerializedName("releaseddate")
    val releasedate: String,
    @SerializedName("director")
    val director: String,
    @SerializedName("imagelink")
    val imagelink: String,
    @SerializedName("synopsis")
    val synopis: String
)