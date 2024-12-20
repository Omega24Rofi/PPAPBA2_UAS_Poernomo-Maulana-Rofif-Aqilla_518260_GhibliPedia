package com.example.ghiblipedia.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ghiblipedia.databinding.ActivityAdminCreateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.ghiblipedia.Model.Movie.Movie
import com.example.ghiblipedia.Network.ApiClient

class AdminCreateActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAdminCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminCreateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.buttonSubmit.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        // Get values from form fields
        val title = binding.editTextMovieName.text.toString()
        val releaseDate = binding.editTextReleaseDate.text.toString()
        val director = binding.editTextDirectorName.text.toString()
        val imageLink = binding.editTextImageLink.text.toString()
        val synopsis = binding.editTextSynopsis.text.toString()

        // Create movie object
        val movie = Movie(
            id = null, // ID will be generated by the server
            title = title,
            releasedate = releaseDate,
            director = director,
            imagelink = imageLink,
            synopis = synopsis
        )

        // Make API call
        ApiClient.getInstance().createMovie(movie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AdminCreateActivity,
                        "Movie created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Return to previous screen
                } else {
                    Toast.makeText(
                        this@AdminCreateActivity,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Toast.makeText(
                    this@AdminCreateActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}