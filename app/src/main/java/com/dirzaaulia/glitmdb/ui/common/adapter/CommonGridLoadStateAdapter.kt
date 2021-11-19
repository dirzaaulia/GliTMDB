package com.dirzaaulia.glitmdb.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.glitmdb.R
import com.dirzaaulia.glitmdb.databinding.ViewGridLoadStateBinding

class CommonGridLoadStateAdapter(
    private val retry: () -> Unit
): LoadStateAdapter<CommonGridLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    class ViewHolder(
        private val binding: ViewGridLoadStateBinding,
        retry: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                buttonRetry.setOnClickListener { retry.invoke() }
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    errorMessage.text = loadState.error.localizedMessage
                }

                progressBar.isVisible = loadState is LoadState.Loading

                errorMessage.isVisible = loadState is LoadState.Error
                buttonRetry.isVisible = loadState is LoadState.Error
            }
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_grid_load_state, parent, false)
                val binding = ViewGridLoadStateBinding.bind(view)

                return ViewHolder(binding, retry)
            }
        }
    }
}