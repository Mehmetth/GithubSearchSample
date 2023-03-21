package com.mehmetpetek.githubsample.ui.userdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mehmetpetek.githubsample.data.local.model.GeneralInfoModel
import com.mehmetpetek.githubsample.databinding.RvRepoItemBinding

class GeneralInfoAdapter :
    ListAdapter<GeneralInfoModel, GeneralInfoAdapter.GeneralInfoViewHolder>(GeneralInfoDiffUtil()) {

    class GeneralInfoDiffUtil : DiffUtil.ItemCallback<GeneralInfoModel>() {
        override fun areItemsTheSame(oldItem: GeneralInfoModel, newItem: GeneralInfoModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: GeneralInfoModel, newItem: GeneralInfoModel) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralInfoViewHolder {
        return GeneralInfoViewHolder(
            RvRepoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: GeneralInfoViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }

    class GeneralInfoViewHolder(
        val binding: RvRepoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            generalInfoModel: GeneralInfoModel
        ) {
            binding.tvTitle.text = generalInfoModel.title
            binding.tvDesc.text = generalInfoModel.desc
        }
    }
}