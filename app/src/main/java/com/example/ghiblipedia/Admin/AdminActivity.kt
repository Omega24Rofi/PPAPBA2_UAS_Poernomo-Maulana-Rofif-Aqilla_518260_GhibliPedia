package com.example.ghiblipedia.Admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghiblipedia.Auth.PrefManager.PrefManager
import com.example.ghiblipedia.Network.ApiClient
import com.example.ghiblipedia.databinding.ActivityAdminBinding
import com.example.ghiblipedia.databinding.ItemDialogBinding
import com.example.ghiblipedia.Admin.adapter.MovieAdapter
import com.example.ghiblipedia.Model.Movie.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var binding: ActivityAdminBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize PrefManager
        prefManager = PrefManager.getInstance(this)

        setupRecyclerView()
        loadMovies()
        
        binding.btnCreate.setOnClickListener {
            startActivity(
                Intent(
                    this@AdminActivity,
                    AdminCreateActivity::class.java
                )
            )
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(
            onDeleteClick = { movie -> showDeleteDialog(movie) },
            onItemClick = { movie -> 
                Toast.makeText(this, "Clicked: ${movie.title}", Toast.LENGTH_SHORT).show()
                showMovieDetail(movie)
            }
        )
        binding.rvMovies.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(this@AdminActivity)
        }
    }

    private fun loadMovies() {
        ApiClient.getInstance().getAllMovies().enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    movieAdapter.submitList(response.body())
                } else {
                    Toast.makeText(this@AdminActivity, "Failed to load movies", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(this@AdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDeleteDialog(movie: Movie) {
        val builder = AlertDialog.Builder(this)
        val binding = ItemDialogBinding.inflate(layoutInflater)
        val dialog = builder.setView(binding.root)
            .setCancelable(false)
            .create()

        with(binding) {
            dialogTitle.text = "Delete Movie"
            dialogMessage.text = "Are you sure you want to delete ${movie.title}?"
            btnConfirm.text = "Delete"
            btnConfirm.setOnClickListener {
                deleteMovie(movie)
                dialog.dismiss()
            }
            btnCancel.text = "Cancel"
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun deleteMovie(movie: Movie) {
        movie.id?.let { movieId ->
            ApiClient.getInstance().deleteMovie(movieId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminActivity, "Movie deleted", Toast.LENGTH_SHORT).show()
                        loadMovies() // Reload the list
                    } else {
                        Toast.makeText(this@AdminActivity, "Failed to delete movie", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@AdminActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showMovieDetail(movie: Movie) {
        Log.d("AdminActivity", "Showing detail for movie: ${movie.title}")
        
        val intent = Intent(this, AdminDetailActivity::class.java).apply {
            putExtra("movie_id", movie.id)
            putExtra("movie_title", movie.title)
            putExtra("movie_director", movie.director)
            putExtra("movie_date", movie.releasedate)
            putExtra("movie_synopsis", movie.synopis)
            putExtra("movie_image", movie.imagelink)
        }
        startActivity(intent)
    }

    private fun showLogoutDialog() {
        // Inflate the custom dialog layout using ViewBinding
        val builder = AlertDialog.Builder(this)
        val inflate = this.layoutInflater
        val binding = ItemDialogBinding.inflate(inflate)
        val dialog = builder.setView(binding.root)
            .setCancelable(false) // Make dialog non-cancelable by tapping outside
            .create()

        with(binding) {
            dialogTitle.text = "Logout"
            dialogMessage.text = "Do you want to logout?"
            btnConfirm.text = "Yes"
            btnConfirm.setOnClickListener {
                prefManager.clear() // Now this will work since prefManager is initialized
                finish()
            }
            btnCancel.text = "No"
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}