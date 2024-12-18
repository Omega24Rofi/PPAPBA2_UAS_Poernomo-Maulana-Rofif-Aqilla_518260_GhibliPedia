package com.example.ghiblipedia.User.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ghiblipedia.Model.Movie.Movie
import com.example.ghiblipedia.databinding.ItemMovieBinding

class UserMovieAdapter(
    private val onItemClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit
) : ListAdapter<Movie, UserMovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(binding) {
                movieTitle.text = movie.title
                movieDirector.text = movie.director
                movieDate.text = movie.releasedate

                // Load image using Glide
                Glide.with(root.context)
                    .load(movie.imagelink)
                    .into(movieImage)

                // Set click listeners
                root.setOnClickListener { onItemClick(movie) }
                btnFavorite.setOnClickListener { onFavoriteClick(movie) }
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
} 