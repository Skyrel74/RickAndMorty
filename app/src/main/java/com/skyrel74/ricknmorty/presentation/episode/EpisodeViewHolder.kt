package com.skyrel74.ricknmorty.presentation.episode

import androidx.recyclerview.widget.RecyclerView
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.databinding.ItemEpisodeBinding

class EpisodeViewHolder(
    private val binding: ItemEpisodeBinding,
    private val onItemClick: (Episode) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Episode) {
        with(binding) {
            tvName.text = getStringInTemplate(R.string.episode_name_template, item.name)
            tvEpisode.text = getStringInTemplate(R.string.episode_episode_template, item.episode)
            tvAirDate.text = getStringInTemplate(R.string.episode_air_date_template, item.airDate)
            itemEpisode.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun getStringInTemplate(stringId: Int, text: String) =
        String.format(itemView.resources.getString(stringId), text)
}