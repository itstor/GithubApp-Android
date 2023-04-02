package com.itstor.githubapp.view

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.githubapp.R
import com.itstor.githubapp.databinding.ActivityMainBinding
import com.itstor.githubapp.models.UserResponse
import com.itstor.githubapp.viewmodel.MainViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var searchTextChangedJob: Job? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val userListLayoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = userListLayoutManager

        binding.rvUserList.addItemDecoration(MarginItemDecoration(24))

        viewModel.listUser.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
            } else {
                binding.tvNotFound.visibility = View.GONE
            }

            val userListAdapter = UserListAdapter(it)
            userListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserResponse) {
                    val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                    startActivity(intent)
                }
            })
            binding.rvUserList.adapter = userListAdapter
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
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

    private fun showLoading() {
        binding.pbUserList.visibility = View.VISIBLE
        binding.rvUserList.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.pbUserList.visibility = View.GONE
        binding.rvUserList.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}