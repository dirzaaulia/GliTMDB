package com.dirzaaulia.glitmdb.data.model

import com.squareup.moshi.Json

data class Video(
    val name: String?,
    val key: String?,
    val site: String?,
    val size: Int?,
    val type: String?,
    val official: Boolean?,
    val id: String?,
    @Json(name = "published_at")
    val publishedAt: String?
)