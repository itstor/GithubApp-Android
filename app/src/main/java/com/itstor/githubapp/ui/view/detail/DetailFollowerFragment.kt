package com.itstor.githubapp.ui.view.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.githubapp.databinding.FragmentDetailFollowerBinding
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.ui.common.UserListAdapter
import com.itstor.githubapp.ui.common.MarginItemDecoration

class DetailFollowerFragment : Fragment() {
    private lateinit var binding: FragmentDetailFollowerBinding
    private lateinit var parentViewModel: DetailUserViewModel
    private lateinit var followerListAdapter: UserListAdapter<UserResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailFollowerBinding.inflate(layoutInflater, container, false)
        parentViewModel = ViewModelProvider(requireActivity())[DetailUserViewModel::class.java]

        val userListLayoutManager = LinearLayoutManager(context)
        binding.rvFollowerList.layoutManager = userListLayoutManager

        binding.rvFollowerList.addItemDecoration(MarginItemDecoration(24))

        followerListAdapter = UserListAdapter()

        parentViewModel.listFollower.observe(viewLifecycleOwner) {
            followerListAdapter.setData(it)

            followerListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback<UserResponse> {
                override fun onItemClicked(data: UserResponse) {
                    val intent = Intent(context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                    startActivity(intent)
                }
            })
            binding.rvFollowerList.adapter = followerListAdapter
        }

        parentViewModel.isLoadingFollower.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbFollowerList.visibility = View.VISIBLE
            } else {
                binding.pbFollowerList.visibility = View.GONE
            }
        }

        return binding.root
    }
}