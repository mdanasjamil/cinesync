package com.example.cinesync.domain.Entity

data class Movie (
    val id: Int,
    val title: String,
    val posterPath: String,
    val voteAverage: Double,
){
    override fun equals(other: Any?): Boolean {

        other as Movie

        if (title != other.title) return false
        if (posterPath != other.posterPath) return false
        if (voteAverage != other.voteAverage) return false

        return true
    }
}