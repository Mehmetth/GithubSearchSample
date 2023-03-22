package com.mehmetpetek.githubsample.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehmetpetek.githubsample.R
import com.mehmetpetek.githubsample.common.extensions.ScaleType
import com.mehmetpetek.githubsample.common.extensions.setImage
import com.mehmetpetek.githubsample.data.remote.model.Users
import com.mehmetpetek.githubsample.databinding.RvUserItemBinding

class UsersAdapter(
    private val onUserListener: OnUserListener
) : ListAdapter<Users, UsersAdapter.UsersViewHolder>(UsersDiffUtil()) {

    class UsersDiffUtil : DiffUtil.ItemCallback<Users>() {
        override fun areItemsTheSame(oldItem: Users, newItem: Users) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Users, newItem: Users) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            RvUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onUserListener
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    fun updateFavIcon(index: Int) {
        currentList[index].favorite = !(currentList[index].favorite)
        notifyItemChanged(index)
    }

    class UsersViewHolder(
        private val binding: RvUserItemBinding,
        private val onUserListener: OnUserListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            user: Users
        ) {
            binding.ivUserIcon.setImage(user.avatar_url, ScaleType.CENTER_INSIDE)
            binding.tvUserName.text = user.login

            binding.ivFavIcon.setImageResource(if (user.favorite) R.drawable.ic_favorite else R.drawable.ic_unfavorite)

            binding.ivFavIcon.setOnClickListener {
                onUserListener.onSelectFavoriteClicked(absoluteAdapterPosition, user.id)
            }

            binding.root.setOnClickListener {
                onUserListener.onSelectUserClicked(user.login)
            }
        }
    }

    interface OnUserListener {
        fun onSelectUserClicked(login: String)
        fun onSelectFavoriteClicked(index: Int, id: Int)
    }
}