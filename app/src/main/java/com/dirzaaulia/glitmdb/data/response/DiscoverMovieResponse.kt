package com.dirzaaulia.glitmdb.data.response

import com.dirzaaulia.glitmdb.data.model.Movie
import com.squareup.moshi.Json

data class DiscoverMovieResponse (
    val page: Int?,
    val results: List<Movie>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?,
)