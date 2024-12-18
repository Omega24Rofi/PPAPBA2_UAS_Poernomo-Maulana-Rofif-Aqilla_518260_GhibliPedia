package com.example.ghiblipedia.User.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ghiblipedia.Auth.PrefManager.PrefManager
import com.example.ghiblipedia.Database.AppDatabase
import com.example.ghiblipedia.Database.FavoriteMovie
import com.example.ghiblipedia.Model.Movie.Movie
import com.example.ghiblipedia.User.DetailActivity
import com.example.ghiblipedia.User.adapter.UserMovieAdapter
import com.example.ghiblipedia.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: UserMovieAdapter
    private lateinit var db: AppDatabase
    private lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        db = AppDatabase.getDatabase(requireContext())
        prefManager = PrefManager.getInstance(requireContext())
        
        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        movieAdapter = UserMovieAdapter(
            onItemClick = { movie -> showMovieDetail(movie) },
            onFavoriteClick = { movie -> removeFromFavorites(movie) }
        )
        binding.recycleView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadFavorites() {
        val userId = prefManager.getId()
        db.favoriteMovieDao().getFavorites(userId).observe(viewLifecycleOwner) { favorites ->
            val movies = favorites.map { favorite ->
                Movie(
                    id = favorite.movieId,
                    title = favorite.title,
                    director = favorite.director,
                    releasedate = favorite.releasedate,
                    synopis = favorite.synopis,
                    imagelink = favorite.imagelink
                )
            }
            movieAdapter.submitList(movies)
        }
    }

    private fun removeFromFavorites(movie: Movie) {
        val userId = prefManager.getId()
        lifecycleScope.launch {
            db.favoriteMovieDao().removeFromFavorites(
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 