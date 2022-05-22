package com.example.moviesapp.viewmodel



import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.MovieCardClickListener
import com.example.moviesapp.R
import com.example.moviesapp.databinding.CardMovieBinding
import com.example.moviesapp.model.Movie


class MoviesAdapter: ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(DiffCallback){

    var listener : MovieCardClickListener? = null

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MovieViewHolder(private var binding: CardMovieBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(movie: Movie){
            if(movie.rating==null){
                binding.ratingTxtView.text = ""
                binding.starIcon.visibility = View.INVISIBLE
            }
            else{
                binding.ratingTxtView.text = movie.rating.toString()
            }

            binding.directorTxtView.text = movie.director
            binding.titleTxtView.text = movie.title
            binding.yearTxtView.text = if(movie.year==null){""}else{movie.year.toString()}
            binding.movieCard.setOnClickListener{listener?.onMovieCardClick(movie)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            CardMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getMovieByRecyclerPosition(position : Int) : Movie{
        return currentList[position]
    }



    fun filterList(list: List<Movie>?, watchedStatus: Boolean) {
        if(list!=null){
            val filteredList = mutableListOf<Movie>()
            if(watchedStatus){
                for (movie in list!!) {
                    if(movie.watched){
                        filteredList.add(movie)
                    }
                }
            }
            else{
                for (movie in list!!) {
                    if(!movie.watched){
                        filteredList.add(movie)
                    }
                }
            }
            submitList(filteredList)
        }
    }
}