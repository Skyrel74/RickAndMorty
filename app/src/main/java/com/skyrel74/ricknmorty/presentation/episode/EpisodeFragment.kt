package com.skyrel74.ricknmorty.presentation.episode

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Episode
import com.skyrel74.ricknmorty.databinding.FragmentEpisodeBinding
import com.skyrel74.ricknmorty.presentation.episode.EpisodeAdapter.Companion.VISIBLE_THRESHOLD
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EpisodeFragment : DaggerFragment(R.layout.fragment_episode) {

    private val binding by viewBinding(FragmentEpisodeBinding::bind)
    private var episodeAdapter: EpisodeAdapter? = null

    @Inject
    lateinit var viewModel: EpisodeViewModel

    private val compositeDisposable = CompositeDisposable()

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(rvEpisode) {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = EpisodeAdapter {
                    // Do smth on item click
                }.also { episodeAdapter = it }
                addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
                addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL))
            }
            swipeContainer.setOnRefreshListener {
                refreshData()
            }

            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
        }

        setupListListener()
        subscribeForData()
    }

    private fun setupListListener() {
        with(binding.rvEpisode) {
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
            .onBackpressureDrop()
            .doOnSubscribe {
                binding.progressBar.visibility = View.VISIBLE
            }
            .concatMap { page: Int ->
                viewModel.pageNumber = page
                viewModel.getEpisodes().toFlowable(BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items: List<Episode> ->
                val episodes = episodeAdapter!!.currentList.plus(items)
                episodeAdapter!!.submitList(episodes)
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
            .subscribe({ items: List<Episode> ->
                episodeAdapter!!.submitList(items)
            }, this::showError)
        compositeDisposable.add(characters)
    }

    private fun showError(e: Throwable) {
        Log.e("EpisodeFragment", e.stackTraceToString())
        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        episodeAdapter = null
        super.onDestroy()
    }
}