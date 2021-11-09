package com.skyrel74.ricknmorty.presentation.location

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.data.entities.Location
import com.skyrel74.ricknmorty.databinding.FragmentLocationBinding
import com.skyrel74.ricknmorty.presentation.episode.EpisodeAdapter.Companion.VISIBLE_THRESHOLD
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LocationFragment : DaggerFragment(R.layout.fragment_location) {

    private val binding by viewBinding(FragmentLocationBinding::bind)
    private var locationAdapter: LocationAdapter? = null

    @Inject
    lateinit var viewModel: LocationViewModel

    private val compositeDisposable = CompositeDisposable()

    private val paginator: PublishProcessor<Int> = PublishProcessor.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvLocation) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = LocationAdapter {
                // Do smth on item click
            }.also { locationAdapter = it }
        }

        setupListListener()
        subscribeForData()
    }

    private fun setupListListener() {
        with(binding.rvLocation) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = layoutManager!!.itemCount
                    val lastVisibleItem =
                        (layoutManager!! as LinearLayoutManager).findLastVisibleItemPosition()
                    if (totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                        viewModel.pageNumber++
                        paginator.onNext(viewModel.pageNumber)
                    }
                }
            })
        }
    }

    private fun subscribeForData() {
        val pagination = paginator
            .onBackpressureDrop()
            .doOnNext {
                binding.progressBar.visibility = View.VISIBLE
            }
            .concatMap { page: Int ->
                viewModel.pageNumber = page
                viewModel.getLocations().toFlowable(BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items: List<Location> ->
                val characters = locationAdapter!!.currentList.plus(items)
                locationAdapter!!.submitList(characters)
                binding.progressBar.visibility = View.GONE
            }, this::showError)

        compositeDisposable.add(pagination)

        paginator.onNext(viewModel.pageNumber)
    }

    private fun showError(e: Throwable) {
        Log.e("EpisodeFragment", e.stackTraceToString())
        Toast.makeText(requireContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        locationAdapter = null
        super.onDestroy()
    }
}