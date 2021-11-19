package com.dirzaaulia.glitmdb.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.data.model.Review
import com.dirzaaulia.glitmdb.data.model.Video
import com.dirzaaulia.glitmdb.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var currentMovieReviews: Flow<PagingData<Review>>? = null

    private val _movieDetails = MutableLiveData<Movie?>()
    val movieDetails: LiveData<Movie?>
        get() = _movieDetails

    private val _movieVideos = MutableLiveData<List<Video>?>()
    val movieVideos: LiveData<List<Video>?>
        get() = _movieVideos

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                repository.getMovieDetails(movieId).collect {
                    _movieDetails.value = it
                }
                _errorMessage.value = ""
            } catch (exception: Exception) {
                exception.printStackTrace()
                _errorMessage.value = exception.localizedMessage
            }
        }
    }

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            try {
                repository.getMovieVideos(movieId).collect {
                    _movieVideos.value = it
                }
                _errorMessage.value = ""
            } catch (exception: Exception) {
                exception.printStackTrace()
                _errorMessage.value = exception.localizedMessage
            }
        }
    }

    fun getMovieReviews(movieId: Int): Flow<PagingData<Review>> {
        val newResult = repository.getMovieReviews(movieId).cachedIn(viewModelScope)
        currentMovieReviews = newResult
        return newResult
    }
}