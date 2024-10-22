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
    val adult: Boolean? = null,
    val backdrop_path: String? = null,
    val belongs_to_collection: CollectionDetails? = null,
    val budget: Int? = null,
    val homepage: String? = null,
    val imdb_id: String? = null,
    val original_language: String? = null,
    val original_title: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val production_companies: List<ProductionCompany>? = null,
    val production_countries: List<ProductionCountry>? = null,
    val release_date: String,
    val revenue: Int? = null,
    val spoken_languages: List<SpokenLanguage>,
    val status: String? = null,
    val tagline: String? = null,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null
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

data class CastResponse(
    val cast: List<Cast>
)

data class Cast(
    val id: Int,
    val name: String,
    val profile_path: String?
)

data class GenreResponse(
    val genres: List<Genre>
)


data class VideoResponse(
    val results: List<Video>
)

data class Video(
    val key: String,
    val site: String,
    val type: String
)


