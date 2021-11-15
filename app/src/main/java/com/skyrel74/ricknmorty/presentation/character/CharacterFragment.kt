package com.skyrel74.ricknmorty.presentation.character

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
import com.skyrel74.ricknmorty.databinding.FragmentCharacterBinding
import com.skyrel74.ricknmorty.presentation.character.CharacterAdapter.Companion.VISIBLE_THRESHOLD
import com.skyrel74.ricknmorty.presentation.characterDetails.CHARACTER_ID_KEY
import com.skyrel74.ricknmorty.presentation.characterDetails.CharacterDetailsFragment
import com.skyrel74.ricknmorty.presentation.main.MainActivity
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CharacterFragment : DaggerFragment(R.layout.fragment_character) {

    private val binding by viewBinding(FragmentCharacterBinding::bind)
    private var characterAdapter: CharacterAdapter? = null

    @Inject
    lateinit var viewModel: CharacterViewModel

    private val compositeDisposable = CompositeDisposable()

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).showBottomNavigation(true)

        with(binding) {
            with(rvCharacter) {
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
                        ).addToBackStack("qwe").commit()
                    (activity as MainActivity).showBottomNavigation(false)
                }.also { characterAdapter = it }
            }
            swipeContainer.setOnRefreshListener {
                refreshData()
            }

            swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
        }

        setupListListener()
        subscribeForData()
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

    private fun subscribeForData() {
        val pagination = paginator
            .onBackpressureBuffer()
            .doOnSubscribe {
                binding.progressBar.visibility = View.VISIBLE
            }
            .concatMap { page: Int ->
                viewModel.pageNumber = page
                viewModel.getCharacters().toFlowable(BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items: List<Character> ->
                val characters = characterAdapter!!.currentList.plus(items)
                characterAdapter!!.submitList(characters)
                binding.progressBar.visibility = View.GONE
            }, this::showError)

        compositeDisposable.add(pagination)

        paginator.onNext(viewModel.pageNumber)
    }

    private fun refreshData() {
        val characters = viewModel.refresh()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doFinally { binding.swipeContainer.isRefreshing = false }
            .subscribe({ items: List<Character> ->
                characterAdapter!!.submitList(items)
            }, this::showError)
        compositeDisposable.add(characters)
    }

    private fun showError(e: Throwable) {
        Log.e("CharactersFragment", e.stackTraceToString())
        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        characterAdapter = null
        super.onDestroy()
    }
}