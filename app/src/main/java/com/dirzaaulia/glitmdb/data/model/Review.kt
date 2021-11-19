package com.dirzaaulia.glitmdb.data.model

import com.squareup.moshi.Json

data class Review(
    @Json(name = "author_details")
    val authorDetails: Author?,
    val content: String?,
    val id: String?,
    val url: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "updated_at")
    val updatedAt: String?,
)