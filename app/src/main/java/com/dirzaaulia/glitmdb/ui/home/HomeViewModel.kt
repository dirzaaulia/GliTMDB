package com.dirzaaulia.glitmdb.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private var currentDiscoverMovie: Flow<PagingData<Movie>>? = null

    fun discoverMovie(): Flow<PagingData<Movie>> {
        val newResult = repository.discoverMovie().cachedIn(viewModelScope)
        currentDiscoverMovie = newResult
        return newResult
    }

}