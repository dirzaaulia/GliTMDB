package com.dirzaaulia.glitmdb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.network.Service
import com.dirzaaulia.glitmdb.util.TMDB_STARTING_PAGE_INDEX

class PagingSource(
    private val service: Service
): PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: TMDB_STARTING_PAGE_INDEX

        return try {
            val response = service.discoverMovie(page = page)
            val results = response.results

            if (results == null) {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = results,
                    prevKey = if (page == TMDB_STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (page == response.totalPages) null else page + 1
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}