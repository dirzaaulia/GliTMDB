package com.dirzaaulia.glitmdb.data.model

import com.squareup.moshi.Json

data class Author(
    val name: String?,
    val username: String?,
    @Json(name = "avatar_path")
    val avatarPath: String?,
    val rating: Double?
)