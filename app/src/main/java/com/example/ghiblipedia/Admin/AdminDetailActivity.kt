package com.example.ghiblipedia.Admin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ghiblipedia.databinding.ActivityAdminDetailBinding

class AdminDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Get data from intent
        intent.extras?.let { bundle ->
            binding.movieTitle.text = bundle.getString("movie_title")
            binding.movieDirector.text = bundle.getString("movie_director")
            binding.movieDate.text = bundle.getString("movie_date")
            binding.movieDesc.text = bundle.getString("movie_synopsis")
            
            // Load image using Glide or similar library
            Glide.with(this)
                .load(bundle.getString("movie_image"))
                .into(binding.movieImage)
        }

        binding.btnback.setOnClickListener {
            finish()
        }
    }
}