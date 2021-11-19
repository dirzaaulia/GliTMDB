package com.dirzaaulia.glitmdb.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dirzaaulia.glitmdb.data.model.Video
import com.dirzaaulia.glitmdb.databinding.ItemVideoBinding

class VideoAdapter(
    private val listener: VideoAdapterListener
) : ListAdapter<Video, VideoAdapter.ViewHolder>(VideoDiffCallback()) {

    interface VideoAdapterListener {
        fun onItemClicked(item: Video)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
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
        private val binding: ItemVideoBinding,
        private val listener: VideoAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video) {
            binding.apply {
                val thumbnailUrl = "https://img.youtube.com/vi/${item.key}/hqdefault.jpg"
                Glide.with(this.root.context)
                    .load(thumbnailUrl)
                    .into(thumbnail)
                thumbnail.setOnClickListener { listener.onItemClicked(item) }

                name.text = item.name
            }
        }
    }
}

private class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }

}