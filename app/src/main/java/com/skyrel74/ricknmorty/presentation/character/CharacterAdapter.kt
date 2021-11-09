package com.skyrel74.ricknmorty.presentation.character

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.databinding.ItemCharacterBinding
import java.util.*

class CharacterAdapter(
    private val onItemClick: (Character) -> Unit,
) : ListAdapter<Character, CharacterViewHolder>(DiffUtilCallback), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
        holder.bind(getItem(position))


    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val charSearch = charSequence.toString()
            val filterResults = FilterResults()
            val characterList = this@CharacterAdapter.currentList
            filterResults.values = if (charSearch.isEmpty()) {
                characterList
            } else {
                val resultList = mutableListOf<Character>()
                for (character in characterList)
                    if (character.name.lowercase(Locale.ROOT)
                            .contains(charSearch.lowercase(Locale.ROOT))
                    )
                        resultList.add(character)
                resultList
            }
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Character>)
        }
    }

    companion object {

        const val VISIBLE_THRESHOLD = 10

        private object DiffUtilCallback : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Character, newItem: Character) =
                oldItem.name == newItem.name
                        && oldItem.species == newItem.species
                        && oldItem.status == newItem.status
                        && oldItem.gender == newItem.gender
                        && oldItem.image == newItem.image
        }
    }
}

