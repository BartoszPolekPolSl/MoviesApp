package com.example.moviesapp

import com.example.moviesapp.model.Movie

interface MovieCardClickListener {
    fun onMovieCardClick(movie: Movie)
}