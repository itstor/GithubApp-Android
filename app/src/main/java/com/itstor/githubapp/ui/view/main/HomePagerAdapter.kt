package com.itstor.githubapp.ui.view.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = AllUsersFragment()
            1 -> fragment = FavoritesFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}