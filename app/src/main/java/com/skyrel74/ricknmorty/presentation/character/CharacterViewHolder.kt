package com.skyrel74.ricknmorty.presentation.character

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onItemClick: (Character) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Character) {
        with(binding) {
            Glide.with(itemView)
                .load(item.image)
                .skipMemoryCache(true)
                .into(ivCharacter)
            tvName.text = getStringInTemplate(R.string.character_name_template, item.name)
            tvSpecies.text = getStringInTemplate(R.string.character_species_template, item.species)
            tvStatus.text =
                getStringInTemplate(R.string.character_status_template, item.status.toString())
            tvGender.text =
                getStringInTemplate(R.string.character_gender_template, item.gender.toString())
            itemCharacter.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun getStringInTemplate(stringId: Int, text: String) =
        String.format(itemView.resources.getString(stringId), text)
}