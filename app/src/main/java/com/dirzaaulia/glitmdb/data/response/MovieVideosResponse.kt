package com.dirzaaulia.glitmdb.data.response

import com.dirzaaulia.glitmdb.data.model.Video

data class MovieVideosResponse(
    val id: Int?,
    val results: List<Video>?
)