package com.example.ghiblipedia.Admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ghiblipedia.Model.Movie.Movie
import com.example.ghiblipedia.databinding.ItemMovieAdminBinding

class MovieAdapter(
    private val onDeleteClick: (Movie) -> Unit,
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieAdminBinding.inflate(
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
        private val binding: ItemMovieAdminBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(binding) {
                movieTitle.text = movie.title
                movieDirector.text = movie.director
                movieReleaseDate.text = movie.releasedate

                // Load image using Glide
                Glide.with(root.context)
                    .load(movie.imagelink)
                    .into(movieImage)

                // Set click listeners
                btnDelete.setOnClickListener { onDeleteClick(movie) }
                root.setOnClickListener { onItemClick(movie) }
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