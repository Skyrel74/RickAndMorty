package com.skyrel74.ricknmorty.presentation.location

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.databinding.ItemLocationBinding
import java.util.*

class LocationAdapter(
    private val onItemClick: (Location) -> Unit,
) : ListAdapter<Location, LocationViewHolder>(DiffUtilCallback), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) =
        holder.bind(getItem(position))


    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val charSearch = charSequence.toString()
            val filterResults = FilterResults()
            val locationList = this@LocationAdapter.currentList
            filterResults.values = if (charSearch.isEmpty()) {
                locationList
            } else {
                val resultList = mutableListOf<Location>()
                for (location in locationList)
                    if (location.name.lowercase(Locale.ROOT)
                            .contains(charSearch.lowercase(Locale.ROOT))
                    )
                        resultList.add(location)
                resultList
            }
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Location>)
        }
    }

    companion object {

        const val VISIBLE_THRESHOLD = 10

        private object DiffUtilCallback : DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Location, newItem: Location) =
                oldItem.name == newItem.name
                        && oldItem.type == newItem.type
                        && oldItem.dimension == newItem.dimension
        }
    }
}

