package com.itstor.githubapp.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itstor.githubapp.R
import com.itstor.githubapp.databinding.ComponentUserCardBinding
import com.itstor.githubapp.helper.UserDiffCallback
import com.itstor.githubapp.interfaces.SimpleUserInterface

class UserListAdapter<T : SimpleUserInterface> : RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ComponentUserCardBinding): RecyclerView.ViewHolder(binding.root)
    private lateinit var onItemClickCallback: OnItemClickCallback<T>
    private val listUser = ArrayList<T>()

    fun setData(listUser: List<T>) {
        val diffCallback = UserDiffCallback(this.listUser, listUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(listUser)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickCallback<T> {
        fun onItemClicked(data: T)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback<T>) {
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