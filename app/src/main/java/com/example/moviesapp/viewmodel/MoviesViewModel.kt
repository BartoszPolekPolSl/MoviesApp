package com.example.moviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.DB_URL
import com.example.moviesapp.MovieCardClickListener
import com.example.moviesapp.model.Movie
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MoviesViewModel: ViewModel() {

    private val dbMovies = Firebase.database(DB_URL).getReference("movies")

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList : LiveData<List<Movie>>
        get() = _moviesList

    private val valueEventListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val movies = mutableListOf<Movie>()
            for (movieSnapshot in snapshot.children) {
                val movie = movieSnapshot.getValue(Movie::class.java)
                movie?.id = movieSnapshot.key
                movie?.let { movies.add(it) }
            }
            _moviesList.value = movies
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    fun getRealtimeUpdates(){
         dbMovies.addValueEventListener(valueEventListener )
    }

    fun addMovie(movie: Movie){
        movie.id = dbMovies.push().key
        dbMovies.child(movie.id!!).setValue(movie)
    }

    fun updateMovie(movie: Movie){
        dbMovies.child(movie.id!!).setValue(movie)
    }

    fun deleteAuthor(movie: Movie) {
        dbMovies.child(movie.id!!).removeValue()

    }

    fun fetchAuthors() {
        dbMovies.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val movies = mutableListOf<Movie>()
                    for (movieSnapshot in snapshot.children) {
                        val movie = movieSnapshot.getValue(Movie::class.java)
                        movie?.id = movieSnapshot.key
                        movie?.let { movies.add(it) }
                    }
                    _moviesList.value = movies
                }
            }
        })
    }


    override fun onCleared() {
        super.onCleared()
        dbMovies.removeEventListener(valueEventListener)
    }
}