package com.skyrel74.ricknmorty.presentation.characterDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.databinding.FragmentCharacterDetailsBinding
import com.skyrel74.ricknmorty.di.Application
import com.skyrel74.ricknmorty.presentation.characterDetails.CharacterDetailsAdapter.Companion.VISIBLE_THRESHOLD
import com.skyrel74.ricknmorty.presentation.episodeDetails.EPISODE_ID_KEY
import com.skyrel74.ricknmorty.presentation.episodeDetails.EpisodeDetailsFragment
import com.skyrel74.ricknmorty.presentation.locationDetails.LOCATION_ID_KEY
import com.skyrel74.ricknmorty.presentation.locationDetails.LocationDetailsFragment
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

const val CHARACTER_ID_KEY = "CHARACTER_ID_KEY"

class CharacterDetailsFragment : DaggerFragment(R.layout.fragment_character_details) {

    private val binding by viewBinding(FragmentCharacterDetailsBinding::bind)
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModel: CharacterDetailsViewModel

    private var detailsAdapter: CharacterDetailsAdapter? = null

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt(CHARACTER_ID_KEY)

        with(binding.rvEpisodeDetails) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = CharacterDetailsAdapter {
                val fragment = EpisodeDetailsFragment()
                val bundle = Bundle()
                bundle.putInt(EPISODE_ID_KEY, it.id)
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        fragment
                    ).addToBackStack(Application.APP_BACKSTACK).commit()
            }.also { detailsAdapter = it }
        }

        setupData(id)
        setupListListener()
    }

    private fun setupData(id: Int) {
        val characterDetails = viewModel.getCharacterById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ character ->
                setCharacter(character)
                subscribeForData(character.episodeUrls)
            }, {})

        compositeDisposable.add(characterDetails)
    }

    private fun setCharacter(character: Character) {
        with(binding) {
            tvNameDetails.text =
                getStringInTemplate(R.string.character_name_template, character.name)
            tvSpeciesDetails.text =
                getStringInTemplate(R.string.character_species_template, character.species)
            tvGenderDetails.text =
                getStringInTemplate(R.string.character_gender_template, character.gender.toString())
            tvStatusDetails.text =
                getStringInTemplate(R.string.character_status_template, character.status.toString())
            tvLocationDetails.text =
                getStringInTemplate(R.string.character_location_template, character.location.name)
            tvLocationDetails.setOnClickListener {
                replaceLocationFragment(character.location.url)
            }
            tvOriginDetails.text =
                getStringInTemplate(R.string.character_origin_template, character.origin.name)
            tvOriginDetails.setOnClickListener {
                replaceLocationFragment(character.origin.url)
            }
            Glide.with(this@CharacterDetailsFragment)
                .load(character.image)
                .skipMemoryCache(true)
                .into(ivCharacterDetails)
        }
    }

    private fun replaceLocationFragment(url: String) {
        val startIndex = url.lastIndexOf("/") + 1
        val locationId = url.substring(startIndex, url.length).toInt()
        val fragment = LocationDetailsFragment()
        val bundle = Bundle()
        bundle.putInt(LOCATION_ID_KEY, locationId)
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment
            ).addToBackStack(Application.APP_BACKSTACK).commit()
    }

    private fun setupListListener() {
        with(binding.rvEpisodeDetails) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = layoutManager!!.itemCount
                    val lastVisibleItem =
                        (layoutManager!! as LinearLayoutManager).findLastVisibleItemPosition()
                    if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                        paginator.onNext(viewModel.pageNumber + 1)
                    }
                }
            })
        }
    }

    private fun subscribeForData(urlList: List<String>) {
        val pagination = paginator
            .onBackpressureBuffer()
            .concatMap { page: Int ->
                viewModel.pageNumber = page
                viewModel.getMultipleEpisodes(urlList).toFlowable(BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items: List<Episode> ->
                val characters = detailsAdapter!!.currentList.plus(items)
                detailsAdapter!!.submitList(characters)
            }, this::showError)

        compositeDisposable.add(pagination)

        paginator.onNext(viewModel.pageNumber)
    }

    private fun getStringInTemplate(stringId: Int, text: String) =
        String.format(requireContext().resources.getString(stringId), text)

    private fun showError(e: Throwable) {
        Log.e("CharactersFragment", e.stackTraceToString())
        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsAdapter = null
        compositeDisposable.clear()
    }
}