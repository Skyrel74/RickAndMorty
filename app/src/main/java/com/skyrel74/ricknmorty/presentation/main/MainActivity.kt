package com.skyrel74.ricknmorty.presentation.main

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.databinding.ActivityMainBinding
import com.skyrel74.ricknmorty.presentation.character.CharacterFragment
import com.skyrel74.ricknmorty.presentation.episode.EpisodeFragment
import com.skyrel74.ricknmorty.presentation.location.LocationFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RicknMorty)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()
        setListeners()
        replaceFragment(CharacterFragment())
    }

    private fun setBottomNavigationView() =
        binding.btmNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.characters -> {
                    replaceFragment(CharacterFragment())
                    binding.sv.isIconified = true
                    true
                }
                R.id.locations -> {
                    replaceFragment(LocationFragment())
                    binding.sv.isIconified = true
                    true
                }
                R.id.episodes -> {
                    replaceFragment(EpisodeFragment())
                    binding.sv.isIconified = true
                    true
                }
                else -> false
            }
        }

    private fun setListeners() {
        with(binding) {
            supportFragmentManager.addOnBackStackChangedListener {
                when (supportFragmentManager.backStackEntryCount) {
                    0 -> {
                        btmNavView.isVisible = true
                        toolbar.navigationIcon = null
                        sv.isVisible = true
                    }
                    else -> {
                        btmNavView.isVisible = false
                        toolbar.navigationIcon =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_back_button, theme)
                        sv.isVisible = false
                    }
                }
            }
            toolbar.setNavigationOnClickListener {
                this@MainActivity.onBackPressed()
            }
            sv.setOnSearchClickListener {
                when (supportFragmentManager.fragments.last()) {
                    is CharacterFragment -> {
                        characterChips.root.isVisible = true
                        episodeChips.root.isVisible = false
                        locationsChips.root.isVisible = false
                    }
                    is EpisodeFragment -> {
                        characterChips.root.isVisible = false
                        episodeChips.root.isVisible = true
                        locationsChips.root.isVisible = false
                    }
                    is LocationFragment -> {
                        characterChips.root.isVisible = false
                        episodeChips.root.isVisible = false
                        locationsChips.root.isVisible = true
                    }
                    else -> {
                        Log.d("qwe", supportFragmentManager.fragments.last().toString())
                    }
                }
            }
            sv.setOnCloseListener {
                characterChips.root.isVisible = false
                episodeChips.root.isVisible = false
                locationsChips.root.isVisible = false
                sv.onActionViewCollapsed()
                true
            }
        }
    }

    fun setSearchListener(filter: (Map<String, String>) -> (Unit)) {
        with(binding) {
            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        val hashMap: Map<String, String> = getFilters(newText)
                        Log.d("qwe", hashMap.toString())
                        filter(hashMap)
                    }
                    return false
                }
            })
        }
    }

    fun getFilters(newText: String): Map<String, String> {
        val map: MutableMap<String, String> = mutableMapOf()
        with(binding) {
            when (supportFragmentManager.fragments.last()) {
                is CharacterFragment -> {}
                is EpisodeFragment -> {
                    if (episodeChips.chipName.isChecked)
                        map["name"] = newText
                    if (episodeChips.chipEpisode.isChecked)
                        map["episode"] = newText
                }
                is LocationFragment -> {}
                else -> {}
            }
        }
        return map
    }

    private fun replaceFragment(fragment: Fragment, containerId: Int = R.id.fragment_container) =
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()

    override fun onBackPressed() =
        if (supportFragmentManager.backStackEntryCount == 0)
            finish()
        else
            super.onBackPressed()
}