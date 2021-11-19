package com.dirzaaulia.glitmdb.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import java.util.prefs.BackingStoreException

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
)