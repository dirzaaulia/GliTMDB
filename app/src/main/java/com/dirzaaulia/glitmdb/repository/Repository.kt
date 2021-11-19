package com.dirzaaulia.glitmdb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.data.model.Review
import com.dirzaaulia.glitmdb.data.model.Video
import com.dirzaaulia.glitmdb.network.Service
import com.dirzaaulia.glitmdb.paging.MoviePagingSource
import com.dirzaaulia.glitmdb.paging.ReviewPagingSource
import com.dirzaaulia.glitmdb.util.TMDB_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val service: Service
) {

    fun discoverMovie(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = TMDB_PAGE_SIZE),
            pagingSourceFactory = { MoviePagingSource(service) }
        ).flow
    }

    fun getMovieDetails(movieId: Int): Flow<Movie> {
        return flow {
            val movieDetails = service.getMovieDetails(movieId = movieId)
            emit(movieDetails)
        }.flowOn(Dispatchers.IO)
    }

    fun getMovieVideos(movieId: Int): Flow<List<Video>?> {
        return flow {
            val response = service.getMovieVideos(movieId = movieId)
            emit(response.results)
        }.flowOn(Dispatchers.IO)
    }

    fun getMovieReviews(movieId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = TMDB_PAGE_SIZE),
            pagingSourceFactory = { ReviewPagingSource(service, movieId) }
        ).flow
    }
}