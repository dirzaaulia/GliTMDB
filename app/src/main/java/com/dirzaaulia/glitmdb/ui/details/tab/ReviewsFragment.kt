package com.dirzaaulia.glitmdb.ui.details.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.dirzaaulia.glitmdb.BuildConfig
import com.dirzaaulia.glitmdb.R
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.data.model.Review
import com.dirzaaulia.glitmdb.databinding.FragmentReviewsBinding
import com.dirzaaulia.glitmdb.ui.common.adapter.CommonLoadStateAdapter
import com.dirzaaulia.glitmdb.ui.details.DetailsViewModel
import com.dirzaaulia.glitmdb.ui.details.adapter.ReviewAdapter
import com.dirzaaulia.glitmdb.util.isOnline
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReviewsFragment :
    Fragment(),
    ReviewAdapter.ReviewAdapterListener {

    private lateinit var binding: FragmentReviewsBinding

    private var item: Movie? = null
    private var job: Job? = null

    private val viewModel: DetailsViewModel by hiltNavGraphViewModels(R.id.details_nav_graph)
    private val reviewAdapter = ReviewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getParcelable(MOVIE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeMovieReview()
        subscribeErrorMessage()
        setupOnClickListeners()
    }

    override fun onItemClicked(item: Review) {

    }

    private fun subscribeMovieReview() {
        initAdapter()
        refreshMovieReview()
    }

    private fun refreshMovieReview() {
        job?.cancel()
        job = lifecycleScope.launch {
            item?.id?.let { id ->
                viewModel.getMovieReviews(id).collect {
                    reviewAdapter.submitData(it)
                }
            }
        }
    }

    private fun subscribeErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                binding.progressBar.isVisible = false
                binding.viewStatus.status.isVisible = true
                binding.viewStatus.buttonRetry.isVisible = true

                binding.viewStatus.status.text = it
            }
        }
    }

    private fun initAdapter() {
        binding.recyclerView.adapter = reviewAdapter.withLoadStateFooter(
            CommonLoadStateAdapter { reviewAdapter.retry() }
        )

        reviewAdapter.addLoadStateListener { loadState ->
            binding.recyclerView.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.viewStatus.status.isVisible =
                loadState.source.refresh is LoadState.Error
            binding.viewStatus.buttonRetry.isVisible =
                loadState.source.refresh is LoadState.Error
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading

            when (loadState.source.refresh) {
                is LoadState.NotLoading -> {
                    if (reviewAdapter.itemCount == 0) {
                        binding.viewStatus.status.text = getString(R.string.empty_discover_movie)
                    }
                }
                is LoadState.Error -> {
                    if (isOnline(requireContext())) {
                        binding.viewStatus.status.text =
                            (loadState.source.refresh as LoadState.Error).error.localizedMessage
                    } else {
                        binding.viewStatus.status.text = getString(R.string.no_internet)
                    }
                }
                else -> {}
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.viewStatus.buttonRetry.setOnClickListener {
            reviewAdapter.retry()
        }
    }

    companion object {
        private const val MOVIE = BuildConfig.APPLICATION_ID + ".MOVIE"

        @JvmStatic
        fun newInstance(item: Movie) = ReviewsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MOVIE, item)
            }
        }
    }
}