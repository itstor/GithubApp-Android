package com.itstor.githubapp.ui.view.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.itstor.githubapp.R
import com.itstor.githubapp.databinding.ActivityDetailUserBinding
import com.itstor.githubapp.data.source.remote.response.UserResponse
import com.itstor.githubapp.interfaces.SimpleUserInterface
import com.itstor.githubapp.ui.view.main.DetailViewModelFactory
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = obtainViewModel(this)

        // Setup Toolbar
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get User Data from Intent
        val user = intent.getParcelableExtra<SimpleUserInterface>(EXTRA_USER) as SimpleUserInterface

        // Setup Follower and Following Tab
        val detailFollowPagerAdapter = DetailFollowPagerAdapter(this)
        val viewPager: ViewPager2 = binding.vpDetailFollow
        viewPager.adapter = detailFollowPagerAdapter
        val tabs: TabLayout = binding.tabsDetailFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        // Bind data to view
        viewModel.username = user.login
        binding.tvUserName.text = user.login
        binding.cardFollowing.apply {
            tvFollowCardLabel.text = resources.getString(R.string.follow_card_following)
            ivBgIcon.imageTintList = resources.getColorStateList(R.color.follow_card_bg_icon_following, null)
        }
        binding.cardFollower.apply {
            tvFollowCardLabel.text = resources.getString(R.string.follow_card_follower)
            ivBgIcon.imageTintList = resources.getColorStateList(R.color.follow_card_bg_icon_follower, null)
        }
        Glide.with(this)
            .load(user.avatarUrl)
            .into(binding.ivAvatar)

        binding.btnFavorite.setOnClickListener {
            handleFavoriteButton(user)
        }

        viewModel.isLoadingDetail.observe(this) {
            if (it) {
                binding.shimmerTvName.showShimmer(true)
                binding.cardFollower.shimmerTvName.showShimmer(true)
                binding.cardFollowing.shimmerTvName.showShimmer(true)
            } else {
                binding.shimmerTvName.hideShimmer()
                binding.cardFollower.shimmerTvName.hideShimmer()
                binding.cardFollowing.shimmerTvName.hideShimmer()
            }
        }

        viewModel.detailUser.observe(this) {
            binding.tvName.text = it.name
            binding.cardFollower.tvCount.text = it.followers.toString()
            binding.cardFollowing.tvCount.text = it.following.toString()
        }

        viewModel.isUserFavorite(user.id).observe(this) {
            if (it > 0) {
                binding.btnFavorite.imageTintList = resources.getColorStateList(R.color.favorite_btn_color, null)
                binding.btnFavorite.setImageResource(R.drawable.ic_love_fill)
                viewModel.isFavorite = true
            } else {
                binding.btnFavorite.imageTintList = resources.getColorStateList(R.color.unfavorite_btn_color, null)
                binding.btnFavorite.setImageResource(R.drawable.ic_love_outline)
                viewModel.isFavorite = false
            }
        }

        viewModel.showToast.observe(this) {
            it?.let {
                showToast(it)
            }
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun handleFavoriteButton(user: SimpleUserInterface) {
        if (viewModel.isFavorite){
            viewModel.removeFromFavorite(user.id)
        } else {
            viewModel.addToFavorite(user)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "DetailUserActivity"
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_follower,
            R.string.tab_text_following
        )
    }
}