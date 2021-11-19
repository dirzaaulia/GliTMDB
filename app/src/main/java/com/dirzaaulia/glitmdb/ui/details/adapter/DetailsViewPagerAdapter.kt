package com.dirzaaulia.glitmdb.ui.details.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.ui.details.tab.DescriptionFragment
import com.dirzaaulia.glitmdb.ui.details.tab.ReviewsFragment
import com.dirzaaulia.glitmdb.ui.details.tab.VideoFragment

class DetailsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val item: Movie
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return mFragmentCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DescriptionFragment.newInstance(item)
            1 -> ReviewsFragment.newInstance(item)
            2 -> VideoFragment.newInstance(item)
            else -> DescriptionFragment()
        }
    }

    companion object {
        private const val mFragmentCount = 3
    }
}