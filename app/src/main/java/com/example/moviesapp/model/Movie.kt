package com.example.moviesapp.model

import com.google.firebase.database.Exclude



data class Movie (
    @get:Exclude
    var id: String?=null,
    val title: String="",
    val director: String?="",
    val year: Int?=null,
    val rating: Int?=null,
    var watched: Boolean=false
        )

enum class WatchedOptions(val bool: Boolean){
    No(false),
    Yes(true)
}
val ratingOptionList = listOf(1,2,3,4,5)