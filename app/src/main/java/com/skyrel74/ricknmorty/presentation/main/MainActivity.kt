package com.skyrel74.ricknmorty.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skyrel74.ricknmorty.R
import com.skyrel74.ricknmorty.databinding.ActivityMainBinding
import com.skyrel74.ricknmorty.presentation.characters.CharactersFragment
import com.skyrel74.ricknmorty.presentation.episodes.EpisodesFragment
import com.skyrel74.ricknmorty.presentation.locations.LocationsFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RicknMorty)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setBottomNavigationView()
    }

    private fun setBottomNavigationView() =
        binding.btmNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.characters -> {
                    replaceFragment(CharactersFragment())
                    true
                }
                R.id.locations -> {
                    replaceFragment(LocationsFragment())
                    true
                }
                R.id.episodes -> {
                    replaceFragment(EpisodesFragment())
                    true
                }
                else -> false
            }
        }

    private fun replaceFragment(fragment: Fragment, containerId: Int = R.id.fragment_container) =
        supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .commit()
}