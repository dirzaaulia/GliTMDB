package com.dirzaaulia.glitmdb.ui.details.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.dirzaaulia.glitmdb.BuildConfig
import com.dirzaaulia.glitmdb.R
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.data.model.Video
import com.dirzaaulia.glitmdb.databinding.FragmentVideoBinding
import com.dirzaaulia.glitmdb.ui.details.DetailsViewModel
import com.dirzaaulia.glitmdb.ui.details.adapter.VideoAdapter
import com.dirzaaulia.glitmdb.util.openYoutube

class VideoFragment :
    Fragment(),
    VideoAdapter.VideoAdapterListener {

    private lateinit var binding: FragmentVideoBinding

    private var item: Movie? = null

    private val viewModel: DetailsViewModel by hiltNavGraphViewModels(R.id.details_nav_graph)

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
        binding = FragmentVideoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMovieVideos()
        subscribeErrorMessage()
        setupOnClickListeners()
    }

    override fun onItemClicked(item: Video) {
        item.key?.let { openYoutube(requireContext(), it) }
    }

    private fun getMovieVideos() {
        item?.id?.let { viewModel.getMovieVideos(it) }
        viewModel.movieVideos.observe(viewLifecycleOwner) {
            setupAdapter(it)
            binding.progressBar.isVisible = false
            binding.viewStatus.status.isVisible = false
            binding.viewStatus.buttonRetry.isVisible = false
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

    private fun setupAdapter(movieVideos: List<Video>?) {
        val adapter = VideoAdapter(this).apply {
            submitList(movieVideos)
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setupOnClickListeners() {
        binding.viewStatus.buttonRetry.setOnClickListener {
            getMovieVideos()
        }
    }

    companion object {
        private const val MOVIE = BuildConfig.APPLICATION_ID + ".MOVIE"

        @JvmStatic
        fun newInstance(item: Movie) = VideoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MOVIE, item)
            }
        }
    }
}