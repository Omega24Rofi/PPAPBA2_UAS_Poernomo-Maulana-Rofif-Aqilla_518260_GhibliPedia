package com.example.ghiblipedia.User.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghiblipedia.Network.ApiClient
import com.example.ghiblipedia.User.DetailActivity
import com.example.ghiblipedia.User.adapter.UserMovieAdapter
import com.example.ghiblipedia.Model.Movie.Movie
import com.example.ghiblipedia.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.ghiblipedia.Database.AppDatabase
import com.example.ghiblipedia.Auth.PrefManager.PrefManager
import com.example.ghiblipedia.Database.FavoriteMovie
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: UserMovieAdapter
    private lateinit var db: AppDatabase
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        db = AppDatabase.getDatabase(requireContext())
        prefManager = PrefManager.getInstance(requireContext())
        
        setupRecyclerView()
        loadMovies()
    }

    private fun setupRecyclerView() {
        movieAdapter = UserMovieAdapter(
            onItemClick = { movie -> showMovieDetail(movie) },
            onFavoriteClick = { movie -> addToFavorites(movie) }
        )
        binding.recycleView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadMovies() {
        ApiClient.getInstance().getAllMovies().enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    movieAdapter.submitList(response.body())
                } else {
                    Toast.makeText(context, "Failed to load movies", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showMovieDetail(movie: Movie) {
        val intent = Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra("movie_id", movie.id)
            putExtra("movie_title", movie.title)
            putExtra("movie_director", movie.director)
            putExtra("movie_date", movie.releasedate)
            putExtra("movie_synopsis", movie.synopis)
            putExtra("movie_image", movie.imagelink)
        }
        startActivity(intent)
    }

    private fun addToFavorites(movie: Movie) {
        val userId = prefManager.getId()
        lifecycleScope.launch {
            db.favoriteMovieDao().addToFavorites(
                FavoriteMovie(
                    movieId = movie.id!!,
                    title = movie.title!!,
                    director = movie.director!!,
                    releasedate = movie.releasedate!!,
                    synopis = movie.synopis!!,
                    imagelink = movie.imagelink!!,
                    userId = userId
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 