package com.galal.movies.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val overview: String,
    val runtime: Int,
    val backdropPath: String,
    val tagline: String,
    val genres: String,
    val spokenLanguages: String,
)
