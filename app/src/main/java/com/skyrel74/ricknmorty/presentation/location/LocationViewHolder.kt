package com.skyrel74.ricknmorty.presentation.location

import androidx.recyclerview.widget.RecyclerView
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.databinding.ItemLocationBinding

class LocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onItemClick: (Location) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Location) {
        with(binding) {
            tvName.text = getStringInTemplate(R.string.location_name_template, item.name)
            tvType.text = getStringInTemplate(R.string.location_type_template, item.type)
            tvDimension.text =
                getStringInTemplate(R.string.location_dimension_template, item.dimension)
            itemLocation.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    private fun getStringInTemplate(stringId: Int, text: String) =
        String.format(itemView.resources.getString(stringId), text)
}