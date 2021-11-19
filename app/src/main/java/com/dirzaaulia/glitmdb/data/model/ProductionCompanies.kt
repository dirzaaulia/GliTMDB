package com.dirzaaulia.glitmdb.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductionCompanies(
    val id: Int?,
    @Json(name = "logo_path")
    val logoPath: String?,
    val name: String?
) : Parcelable