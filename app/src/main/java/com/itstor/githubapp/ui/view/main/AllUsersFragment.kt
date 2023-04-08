package com.itstor.githubapp.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.githubapp.databinding.FragmentAllUsersBinding
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.ui.view.detail.DetailUserActivity
import com.itstor.githubapp.ui.common.MarginItemDecoration
import com.itstor.githubapp.ui.common.UserListAdapter

class AllUsersFragment : Fragment() {
    private lateinit var binding: FragmentAllUsersBinding
    private lateinit var parentViewModel: MainViewModel
    private lateinit var userListAdapter: UserListAdapter<UserResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllUsersBinding.inflate(layoutInflater, container, false)
        parentViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userListLayoutManager = LinearLayoutManager(context)
        binding.rvUserList.layoutManager = userListLayoutManager
        binding.rvUserList.addItemDecoration(MarginItemDecoration(24))

        userListAdapter = UserListAdapter()

        parentViewModel.listAllUsers.observe(viewLifecycleOwner) {
            userListAdapter.setData(it)

            userListAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback<UserResponse> {
                override fun onItemClicked(data: UserResponse) {
                    val intent = Intent(context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USER, data)
                    startActivity(intent)
                }
            })

            binding.rvUserList.adapter = userListAdapter
        }

        parentViewModel.isLoadingAllUser.observe(viewLifecycleOwner) {
            binding.pbUserList.visibility = if (it) View.VISIBLE else View.GONE
            binding.rvUserList.visibility = if (it) View.GONE else View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "AllUsersFragment"
    }
}