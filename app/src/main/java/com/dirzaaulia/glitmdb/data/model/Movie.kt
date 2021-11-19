package com.dirzaaulia.glitmdb.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "genre_ids")
    val genreId: List<Int>?,
    val id: Int?,
    @Json(name = "original_title")
    val originalTitle: String?,
    val overview: String?,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "release_date")
    val releaseDate: String?,
    val title: String?,
    @Json(name = "vote_average")
    val voteAverage: Double?,
    @Json(name = "vote_count")
    val voteCount: Int?,
    val budget: Long?,
    val genres: List<Genre>?,
    val homepage: String?,
    @Json(name = "production_companies")
    val productionCompanies: List<ProductionCompanies>?,
    val revenue: Long?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
) : Parcelable