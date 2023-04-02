package com.itstor.githubapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.githubapp.databinding.FragmentDetailFollowingBinding
import com.itstor.githubapp.models.UserResponse
import com.itstor.githubapp.viewmodel.DetailUserViewModel

class DetailFollowingFragment : Fragment() {
    private lateinit var binding: FragmentDetailFollowingBinding
    private lateinit var parentViewModel: DetailUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailFollowingBinding.inflate(layoutInflater, container, false)
        parentViewModel = ViewModelProvider(requireActivity())[DetailUserViewModel::class.java]

        val userListLayoutManager = LinearLayoutManager(context)
        binding.rvFollowingList.layoutManager = userListLayoutManager

        binding.rvFollowingList.addItemDecoration(MarginItemDecoration(24))

        parentViewModel.listFollowing.observe(viewLifecycleOwner) {
            val followerListAdapter = UserListAdapter(it)
            followerListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserResponse) {
                    val intent = Intent(context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                    startActivity(intent)
                }
            })
            binding.rvFollowingList.adapter = followerListAdapter
        }

        parentViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbFollowingList.visibility = View.VISIBLE
            } else {
                binding.pbFollowingList.visibility = View.GONE
            }
        }

        return binding.root
    }
}