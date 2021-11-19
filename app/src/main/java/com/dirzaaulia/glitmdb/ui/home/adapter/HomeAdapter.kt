package com.dirzaaulia.glitmdb.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.dirzaaulia.glitmdb.R
import com.dirzaaulia.glitmdb.data.model.Movie
import com.dirzaaulia.glitmdb.databinding.ItemHomeBinding
import com.dirzaaulia.glitmdb.util.TMDB_POSTER_URL

class HomeAdapter(
    private val listener: HomeAdapterListener
) : PagingDataAdapter<Movie, HomeAdapter.ViewHolder>(HomeDiffCallback()) {

    interface HomeAdapterListener {
        fun onItemClicked(item: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }


    class ViewHolder(
        private val binding: ItemHomeBinding,
        private val listener: HomeAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.apply {
                val circularProgressDrawable = CircularProgressDrawable(this.root.context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.setColorSchemeColors(
                    ContextCompat.getColor(this.root.context, R.color.purple_700)
                )
                circularProgressDrawable.start()

                Glide.with(this.root.context)
                    .load("$TMDB_POSTER_URL${item.posterPath}")
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(image)

                image.setOnClickListener {
                    listener.onItemClicked(item)
                }
            }
        }
    }
}

private class HomeDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
