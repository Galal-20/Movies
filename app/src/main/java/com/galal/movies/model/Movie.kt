package com.galal.movies.model

data class Movie(
    val id: Int,
    val title: String,
    val release_date: String,
    val poster_path: String
)

data class MovieResponse(
    val results: List<Movie>
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val genres: List<Genre>,
    val runtime: Int,
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: CollectionDetails?,
    val budget: Int,
    val homepage: String?,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class Genre(
    val id: Int,
    val name: String
)

data class CollectionDetails(
    val id: Int,
    val name: String,
    val poster_path: String?,
    val backdrop_path: String?
)

data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguage(
    val iso_639_1: String,
    val name: String
)
