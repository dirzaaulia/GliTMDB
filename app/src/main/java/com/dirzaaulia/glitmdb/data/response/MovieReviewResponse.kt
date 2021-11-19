package com.dirzaaulia.glitmdb.data.response

import com.dirzaaulia.glitmdb.data.model.Review
import com.squareup.moshi.Json

data class MovieReviewResponse(
    val id: Int?,
    val page: Int?,
    val results: List<Review>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?
)