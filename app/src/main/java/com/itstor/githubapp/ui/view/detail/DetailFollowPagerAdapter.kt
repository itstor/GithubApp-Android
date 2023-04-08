package com.itstor.githubapp.ui.view.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailFollowPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DetailFollowerFragment()
            1 -> fragment = DetailFollowingFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}