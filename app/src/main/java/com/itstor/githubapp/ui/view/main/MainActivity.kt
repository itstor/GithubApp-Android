package com.itstor.githubapp.ui.view.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import com.itstor.githubapp.R
import com.itstor.githubapp.data.SettingPreferences
import com.itstor.githubapp.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var searchTextChangedJob: Job? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var settingPreferences: SettingPreferences
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreferences = SettingPreferences.getInstance(dataStore)

        viewModel = obtainViewModel(this)

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        openHomeFragment()

        binding.btnSwitchTheme.setOnClickListener {
            lifecycleScope.launch {
                viewModel.switchTheme()
            }
        }

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnSwitchTheme.setImageResource(R.drawable.ic_moon)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnSwitchTheme.setImageResource(R.drawable.ic_sun)
            }
        }

        viewModel.showToast.observe(this) {
            it?.let {
                showToast(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.main_toolbar_search_hint)

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                openSearchFragment()

                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                openHomeFragment()

                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()

                viewModel.searchQuery = query

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchTextChangedJob?.cancel()

                searchTextChangedJob =  lifecycleScope.launch(Dispatchers.Main) {
                    if (newText != null && newText.isNotEmpty()) {
                        delay(500)
                    }

                    viewModel.searchQuery = newText
                }

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = MainViewModelFactory.getInstance(activity.application, settingPreferences)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private fun openHomeFragment() {
        val homeFragment = HomeFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(binding.frameContainer.id, homeFragment, HomeFragment::class.java.simpleName)
            commit()
        }
    }

    private fun openSearchFragment() {
        val searchFragment = SearchFragment()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(binding.frameContainer.id, searchFragment, SearchFragment::class.java.simpleName)
            commit()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}