package com.example.moviesapp.model

import com.google.firebase.database.Exclude



data class Movie (
    @get:Exclude
    var id: String?=null,
    val title: String="",
    val director: String="",
    val year: Int=0,
    val rating: Int=0,
    val watched: Boolean=false
        )

enum class WatchedOptions{
    Yes,
    No
}
val ratingOptionList = listOf(1,2,3,4,5)