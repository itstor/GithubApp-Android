package com.itstor.githubapp.ui.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.itstor.githubapp.R
import com.itstor.githubapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var parentViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        parentViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup TabLayout
        val homePagerAdapter = HomePagerAdapter(this)
        val viewPager: ViewPager2 = binding.vpDetailFollow
        viewPager.adapter = homePagerAdapter
        val tabs: TabLayout = binding.tabsDetailFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object {
        private const val TAG = "HomeFragment"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_all_users,
            R.string.tab_text_favorites
        )
    }
}