package com.dirzaaulia.glitmdb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.network.Service
import com.dirzaaulia.glitmdb.paging.PagingSource
import com.dirzaaulia.glitmdb.util.TMDB_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val service: Service
) {

    fun discoverMovie(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = TMDB_PAGE_SIZE),
            pagingSourceFactory ={ PagingSource(service) }
        ).flow
    }
}