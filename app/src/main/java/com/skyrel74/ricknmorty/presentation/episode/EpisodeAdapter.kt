package com.skyrel74.ricknmorty.presentation.episode

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.databinding.ItemEpisodeBinding
import java.util.*

class EpisodeAdapter(
    private val onItemClick: (Episode) -> Unit,
) : ListAdapter<Episode, EpisodeViewHolder>(DiffUtilCallback), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEpisodeBinding.inflate(inflater, parent, false)
        return EpisodeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) =
        holder.bind(getItem(position))


    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val charSearch = charSequence.toString()
            val filterResults = FilterResults()
            val episodeList = this@EpisodeAdapter.currentList
            filterResults.values = if (charSearch.isEmpty()) {
                episodeList
            } else {
                val resultList = mutableListOf<Episode>()
                for (episode in episodeList)
                    if (episode.name.lowercase(Locale.ROOT)
                            .contains(charSearch.lowercase(Locale.ROOT))
                    )
                        resultList.add(episode)
                resultList
            }
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Episode>)
        }
    }

    companion object {

        const val VISIBLE_THRESHOLD = 16

        private object DiffUtilCallback : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode) =
                oldItem.name == newItem.name
                        && oldItem.episode == newItem.episode
                        && oldItem.airDate == newItem.airDate
        }
    }
}

