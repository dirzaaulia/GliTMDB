package com.dirzaaulia.glitmdb.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dirzaaulia.glitmdb.DetailsNavGraphArgs
import com.dirzaaulia.glitmdb.R
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.databinding.FragmentDetailsBinding
import com.dirzaaulia.glitmdb.ui.details.adapter.DetailsViewPagerAdapter
import com.dirzaaulia.glitmdb.util.TMDB_POSTER_URL
import com.dirzaaulia.glitmdb.util.dateFormatter
import com.dirzaaulia.glitmdb.util.isOnline
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    private val args: DetailsNavGraphArgs by navArgs()
    private val viewModel: DetailsViewModel by hiltNavGraphViewModels(R.id.details_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupOnClickListeners()
        getMovieDetails()
        subscribeErrorMessage()
    }

    private fun initUI() {
        binding.viewStatus.status.isVisible = false
        binding.viewStatus.buttonRetry.isVisible = false
    }

    private fun getMovieDetails() {
        if (isOnline(requireContext())) {
            binding.progressBar.isVisible = true
            subscribeMovieDetails()
        } else {
            binding.progressBar.isVisible = false
            binding.viewStatus.status.isVisible = true
            binding.viewStatus.buttonRetry.isVisible = true

            binding.viewStatus.status.text = getString(R.string.no_internet)
        }
    }

    private fun subscribeMovieDetails() {
        Timber.i(args.movieId.toString())
        viewModel.getMovieDetails(args.movieId)
        viewModel.movieDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.progressBar.isVisible = false
                binding.container.isVisible = true
                binding.viewStatus.status.isVisible = false
                binding.viewStatus.buttonRetry.isVisible = false

                setMovieDetails(it)
                setupViewPager(it)
            }
        }
    }

    private fun subscribeErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                binding.progressBar.isVisible = false
                binding.container.isVisible = false
                binding.viewStatus.status.isVisible = true
                binding.viewStatus.buttonRetry.isVisible = true

                binding.viewStatus.status.text = viewModel.errorMessage.value
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.viewStatus.buttonRetry.setOnClickListener {
            subscribeMovieDetails()
        }
    }

    private fun setMovieDetails(item: Movie) {
        binding.apply {
            val circularProgressDrawable = CircularProgressDrawable(this.root.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(
                ContextCompat.getColor(this.root.context, R.color.purple_700)
            )
            circularProgressDrawable.start()

            Glide.with(requireContext())
                .load("$TMDB_POSTER_URL${item.posterPath}")
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(poster)

            budget.text = String.format("$%,d", item.budget)

            var genre = ""
            item.genres?.forEach {
                genre += "${it.name} "
            }
            this.genre.text = genre

            var productionCompanies = ""
            item.productionCompanies?.forEach {
                productionCompanies += "${it.name} "
            }
            this.productionCompanies.text = productionCompanies

            releaseDate.text = item.releaseDate?.let {
                dateFormatter(it, "yyyy-MM-dd", "dd MMMM yyyy")
            }

            status.text = item.status

            val voteAverage = item.voteAverage ?: 0
            this.voteAverage.text = "$voteAverage"
        }
    }

    private fun setupViewPager(item: Movie) {
        val tabs = listOf("Description", "Reviews", "Videos")
        val adapter = DetailsViewPagerAdapter(childFragmentManager, lifecycle, item)

        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewpager.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

}