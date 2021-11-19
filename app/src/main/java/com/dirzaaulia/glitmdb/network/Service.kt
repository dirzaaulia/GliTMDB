package com.dirzaaulia.glitmdb.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.glitmdb.BuildConfig
import com.dirzaaulia.glitmdb.data.response.DiscoverMovieResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Service {

    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("include_adult") isIncludeAdult: Boolean = false,
        @Query("include_video") isIncludeVideo: Boolean = false,
        @Query("page") page: Int
    ): DiscoverMovieResponse

    companion object {
        fun create(context: Context): Service {
            val httpInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(httpInterceptor)
                .addInterceptor(chuckerInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.TMDB_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(Service::class.java)
        }
    }
}