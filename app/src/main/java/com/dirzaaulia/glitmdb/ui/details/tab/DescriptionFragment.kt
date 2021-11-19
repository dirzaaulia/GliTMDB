package com.dirzaaulia.glitmdb.ui.details.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.dirzaaulia.glitmdb.BuildConfig
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding

    private var item: Movie? = null

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
        binding = FragmentDescriptionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.apply {
            title.text = item?.title

            if (item?.tagline.isNullOrBlank()) {
                tagline.isVisible = false
            } else {
                tagline.text = item?.tagline
            }

            overview.text = item?.overview
        }
    }

    companion object {
        private const val MOVIE = BuildConfig.APPLICATION_ID + ".MOVIE"

        @JvmStatic
        fun newInstance(item: Movie) = DescriptionFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MOVIE, item)
            }
        }
    }
}