package com.skyrel74.ricknmorty.presentation.characters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.databinding.ItemCharactersBinding

class CharacterViewHolder(
    private val binding: ItemCharactersBinding,
    private val onItemClick: (Character) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Character) {
        with(binding) {
            Glide.with(itemView)
                .load(item.image)
                .skipMemoryCache(true)
                .into(ivCharacter)
            tvName.text = getStringInTemplate(R.string.name_template, item.name)
            tvSpecies.text = getStringInTemplate(R.string.species_template, item.species)
            tvStatus.text = getStringInTemplate(R.string.status_template, item.status)
            tvGender.text = getStringInTemplate(R.string.gender_template, item.gender)
            itemCharacter.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun getStringInTemplate(stringId: Int, text: String) =
        String.format(itemView.resources.getString(stringId), text)
}