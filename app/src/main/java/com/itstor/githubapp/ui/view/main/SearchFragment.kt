package com.itstor.githubapp.ui.view.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itstor.githubapp.R
import com.itstor.githubapp.databinding.FragmentSearchBinding
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.ui.view.detail.DetailUserActivity
import com.itstor.githubapp.ui.common.MarginItemDecoration
import com.itstor.githubapp.ui.common.UserListAdapter

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var parentViewModel: MainViewModel
    private lateinit var userListAdapter: UserListAdapter<UserResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        parentViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userListLayoutManager = LinearLayoutManager(context)
        binding.rvUserList.layoutManager = userListLayoutManager
        binding.rvUserList.addItemDecoration(MarginItemDecoration(24))

        userListAdapter = UserListAdapter()

        parentViewModel.listSearchUser.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showInformationText(resources.getString(R.string.info_empty_result))
            } else {
                hideInformationText()
            }

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

        parentViewModel.isLoadingSearchUser.observe(viewLifecycleOwner) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        binding.pbUserList.visibility = View.VISIBLE
        binding.rvUserList.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.pbUserList.visibility = View.GONE
        binding.rvUserList.visibility = View.VISIBLE
    }

    private fun showInformationText(message: String) {
        binding.tvInfo.text = message
        binding.tvInfo.visibility = View.VISIBLE
    }

    private fun hideInformationText() {
        binding.tvInfo.visibility = View.GONE
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}