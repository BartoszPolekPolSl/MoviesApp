package com.example.moviesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviesapp.DB_URL
import com.example.moviesapp.model.Movie
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MoviesViewModel(private val userId: String): ViewModel() {

    private val dbMovies = Firebase.database(DB_URL).getReference("movies/${userId}")

    private val _watchedFilterStatus : MutableLiveData<Boolean> = MutableLiveData(false)
    val watchedFilterStatus : LiveData<Boolean>
        get() = _watchedFilterStatus

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList : LiveData<List<Movie>>
        get() = _moviesList

    private val valueEventListener = object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val movies = mutableListOf<Movie>()
            for (movieSnapshot in snapshot.children) {
                val movie = movieSnapshot.getValue(Movie::class.java)
                movie?.id = movieSnapshot.key
                movie?.let {movies.add(it)}
            }
            _moviesList.value = movies
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    fun changeFilterStatus(){
        _watchedFilterStatus.value = !_watchedFilterStatus.value!!
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

    fun changeWatched(movie: Movie){
        if(movie.watched){
            movie.watched=false
            dbMovies.child(movie.id!!).setValue(movie)
        }
        else{
            movie.watched=true
            dbMovies.child(movie.id!!).setValue(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        dbMovies.child(movie.id!!).removeValue()

    }


    override fun onCleared() {
        super.onCleared()
        dbMovies.removeEventListener(valueEventListener)
    }
}

class MoviesViewModelFactory(private val userId: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel(userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}