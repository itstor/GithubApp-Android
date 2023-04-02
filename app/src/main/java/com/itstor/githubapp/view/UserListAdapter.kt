package com.itstor.githubapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itstor.githubapp.R
import com.itstor.githubapp.databinding.ComponentUserCardBinding
import com.itstor.githubapp.models.UserResponse

class UserListAdapter(private val listUser: List<UserResponse>) : RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ComponentUserCardBinding): RecyclerView.ViewHolder(binding.root)
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: UserResponse)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ComponentUserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.binding.apply {
            tvUserName.text = user.login

            Glide.with(holder.itemView.context)
                .load(user.avatarUrl)
                .apply(RequestOptions().override(55, 55))
                .into(ivMiniAvatar)

            chipUserType.text = user.type
            chipUserType.chipBackgroundColor = when (user.type) {
                "User" -> root.resources.getColorStateList(R.color.chip_green_50, null)
                else -> root.resources.getColorStateList(R.color.chip_blue_50, null)
            }

            chipUserType.setTextColor(when (user.type) {
                "User" -> root.resources.getColor(R.color.chip_green, null)
                else -> root.resources.getColor(R.color.chip_blue, null)
            })

            root.setOnClickListener {
                onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = listUser.size
}