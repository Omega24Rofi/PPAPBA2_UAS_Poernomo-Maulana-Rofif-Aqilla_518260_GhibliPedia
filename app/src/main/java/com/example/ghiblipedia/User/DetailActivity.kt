package com.example.ghiblipedia.User

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ghiblipedia.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get movie details from intent
        val movieId = intent.getStringExtra("movie_id")
        val movieTitle = intent.getStringExtra("movie_title")
        val movieDirector = intent.getStringExtra("movie_director")
        val movieDate = intent.getStringExtra("movie_date")
        val movieSynopsis = intent.getStringExtra("movie_synopsis")
        val movieImageUrl = intent.getStringExtra("movie_image")

        // Set the values to views
        with(binding) {
            movieTitle?.let { title -> this.movieTitle.text = title }
            movieDirector?.let { director -> this.movieDirector.text = director }
            movieDate?.let { date -> this.movieDate.text = date }
            movieSynopsis?.let { synopsis -> this.movieDesc.text = synopsis }
            
            // Load movie image
            movieImageUrl?.let { imageUrl ->
                Glide.with(this@DetailActivity)
                    .load(imageUrl)
                    .into(movieImage)
            }

            // Set back button click listener
            backButton.setOnClickListener {
                finish()
            }
        }
    }
}