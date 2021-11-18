package com.skyrel74.ricknmorty.presentation.episodeDetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Character
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.databinding.FragmentEpisodeDetailsBinding
import com.skyrel74.ricknmorty.di.Application.Companion.APP_BACKSTACK
import com.skyrel74.ricknmorty.presentation.character.CharacterAdapter
import com.skyrel74.ricknmorty.presentation.character.CharacterAdapter.Companion.VISIBLE_THRESHOLD
import com.skyrel74.ricknmorty.presentation.characterDetails.CHARACTER_ID_KEY
import com.skyrel74.ricknmorty.presentation.characterDetails.CharacterDetailsFragment
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

const val EPISODE_ID_KEY = "EPISODE_ID_KEY"

class EpisodeDetailsFragment : DaggerFragment(R.layout.fragment_episode_details) {

    private val binding by viewBinding(FragmentEpisodeDetailsBinding::bind)
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModel: EpisodeDetailsViewModel

    private var characterAdapter: CharacterAdapter? = null

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt(EPISODE_ID_KEY)

        with(binding.rvCharacter) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = CharacterAdapter {
                val fragment = CharacterDetailsFragment()
                val bundle = Bundle()
                bundle.putInt(CHARACTER_ID_KEY, it.id)
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        fragment
                    ).addToBackStack(APP_BACKSTACK).commit()
            }.also { characterAdapter = it }
        }

        setupData(id)
        setupListListener()
    }

    private fun setupData(id: Int) {
        val characterDetails = viewModel.getEpisodeById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ episode ->
                setEpisode(episode)
                subscribeForData(episode.characters)
            }, {})

        compositeDisposable.add(characterDetails)
    }

    private fun setEpisode(episode: Episode) {
        with(binding) {
            tvNameDetails.text =
                getStringInTemplate(R.string.episode_name_template, episode.name)
            tvEpisodeDetails.text =
                getStringInTemplate(R.string.episode_episode_template, episode.episode)
            tvAirDateDetails.text =
                getStringInTemplate(R.string.episode_air_date_template, episode.airDate)
        }
    }

    private fun setupListListener() {
        with(binding.rvCharacter) {
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
                viewModel.getMultipleCharacters(urlList).toFlowable(BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items: List<Character> ->
                val characters = characterAdapter!!.currentList.plus(items)
                characterAdapter!!.submitList(characters)
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
        characterAdapter = null
        compositeDisposable.clear()
    }
}