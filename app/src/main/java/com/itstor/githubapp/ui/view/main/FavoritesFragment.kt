package com.itstor.githubapp.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.githubapp.data.source.local.entity.FavoriteEntity
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.databinding.FragmentFavoritesBinding
import com.itstor.githubapp.ui.common.MarginItemDecoration
import com.itstor.githubapp.ui.common.UserListAdapter
import com.itstor.githubapp.ui.view.detail.DetailUserActivity

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var parentViewModel: MainViewModel
    private lateinit var favoriteListAdapter: UserListAdapter<FavoriteEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        parentViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUserList.layoutManager = layoutManager
        binding.rvUserList.addItemDecoration(MarginItemDecoration(24))

        favoriteListAdapter = UserListAdapter()

        parentViewModel.getFavoriteUser().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvInfo.visibility = View.VISIBLE
            } else {
                binding.tvInfo.visibility = View.GONE
            }

            favoriteListAdapter.setData(it)
            favoriteListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback<FavoriteEntity> {
                override fun onItemClicked(data: FavoriteEntity) {
                    val intent = Intent(context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                    startActivity(intent)
                }
            })
            binding.rvUserList.adapter = favoriteListAdapter
        }
    }
}