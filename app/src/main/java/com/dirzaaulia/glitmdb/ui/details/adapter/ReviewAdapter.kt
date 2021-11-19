package com.dirzaaulia.glitmdb.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dirzaaulia.glitmdb.data.model.Review
import com.dirzaaulia.glitmdb.databinding.ItemReviewBinding

class ReviewAdapter(
    private val listener: ReviewAdapterListener
) : PagingDataAdapter<Review, ReviewAdapter.ViewHolder>(ReviewDiffCallback()) {

    interface ReviewAdapterListener {
        fun onItemClicked(item: Review)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            listener
        )
    }

    class ViewHolder(
        private val binding: ItemReviewBinding,
        private val listener: ReviewAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            binding.apply {
                val avatarUrl =
                    item.authorDetails?.avatarPath?.replaceFirst("/", "")

                if (avatarUrl.isNullOrBlank()) {
                    avatar.isVisible = false
                } else {
                    Glide.with(this.root.context)
                        .load(avatarUrl)
                        .into(avatar)
                }

                username.text = item.authorDetails?.username

                val rating = item.authorDetails?.rating ?: 0
                this.rating.text = "$rating"

                review.text = item.content

                this.root.setOnClickListener { listener.onItemClicked(item) }
            }
        }
    }
}

private class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {

    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}