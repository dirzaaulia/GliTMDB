package com.dirzaaulia.glitmdb.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.dirzaaulia.glitmdb.R
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.databinding.FragmentHomeBinding
import com.dirzaaulia.glitmdb.ui.common.adapter.CommonLoadStateAdapter
import com.dirzaaulia.glitmdb.ui.home.adapter.HomeAdapter
import com.dirzaaulia.glitmdb.util.isOnline
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    Fragment(),
    HomeAdapter.HomeAdapterListener {

    private lateinit var binding: FragmentHomeBinding

    private var job: Job? = null

    private val viewModel: HomeViewModel by viewModels()
    private val homeAdapter = HomeAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
        subscribeDiscoverMovie()
    }

    override fun onItemClicked(item: Movie) {
        val directions = item.id?.let {
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
        }
        directions?.let { view?.findNavController()?.navigate(it) }
    }

    private fun setupOnClickListeners() {
        binding.viewStatus.buttonRetry.setOnClickListener { homeAdapter.retry() }
    }

    private fun subscribeDiscoverMovie() {
        initAdapter()
        refreshDiscoverMovie()
    }

    private fun initAdapter() {
        binding.recyclerView.adapter = homeAdapter.withLoadStateFooter(
            CommonLoadStateAdapter { homeAdapter.retry() }
        )

        homeAdapter.addLoadStateListener { loadState ->
            binding.recyclerView.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            binding.viewStatus.status.isVisible =
                loadState.source.refresh is LoadState.Error
            binding.viewStatus.buttonRetry.isVisible =
                loadState.source.refresh is LoadState.Error
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading

            when (loadState.source.refresh) {
                is LoadState.NotLoading -> {
                    if (homeAdapter.itemCount == 0) {
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

    private fun refreshDiscoverMovie() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.discoverMovie().collectLatest {
                homeAdapter.submitData(it)
            }
        }
    }

}