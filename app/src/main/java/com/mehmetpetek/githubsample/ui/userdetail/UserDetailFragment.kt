package com.mehmetpetek.githubsample.ui.userdetail

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mehmetpetek.githubsample.R
import com.mehmetpetek.githubsample.common.extensions.ScaleType
import com.mehmetpetek.githubsample.common.extensions.setImage
import com.mehmetpetek.githubsample.common.extensions.toDate
import com.mehmetpetek.githubsample.data.local.model.GeneralInfoModel
import com.mehmetpetek.githubsample.data.remote.model.UserDetailResponse
import com.mehmetpetek.githubsample.databinding.FragmentUserDetailBinding
import com.mehmetpetek.githubsample.ui.base.BaseFragment
import com.mehmetpetek.githubsample.ui.userdetail.adapter.GeneralInfoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailFragment :
    BaseFragment<FragmentUserDetailBinding>(FragmentUserDetailBinding::inflate) {

    private val viewModel by viewModels<UserDetailViewModel>()

    override fun bindScreen() {
        getEffect()
        getState()
    }

    private fun getEffect() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.effect.collect {
                    when (it) {
                        is UserDetailEffect.ShowError -> {
                            handleError(it.throwable)
                        }
                    }
                }
            }
        }
    }

    private fun getState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect {
                    setLoadingState(it.isLoading)
                    setFavIcon(it.isFav)
                    setUserProperties(it.userDetailResponse)

                    binding.ivFavIcon.setOnClickListener {
                        viewModel.setEvent(UserDetailEvent.ClickFavorite)
                    }
                }
            }
        }
    }

    private fun setFavIcon(value: Boolean) {
        binding.ivFavIcon.setImageResource(if (value) R.drawable.ic_favorite else R.drawable.ic_unfavorite)
    }

    private fun setUserProperties(userDetailResponse: UserDetailResponse?) {
        binding.ivUserIcon.setImage(
            userDetailResponse?.avatar_url ?: "",
            ScaleType.CENTER_INSIDE
        )
        binding.tvUserNameDesc.text = userDetailResponse?.login
        binding.tvUserRealNameDesc.text = userDetailResponse?.name.toString()
        binding.tvUserReposDesc.text = userDetailResponse?.public_repos.toString()

        //Added for Sample User
        val list: MutableList<GeneralInfoModel> = mutableListOf()
        list.add(GeneralInfoModel(getString(R.string.company), userDetailResponse?.company ?: "-"))
        list.add(
            GeneralInfoModel(
                getString(R.string.location),
                userDetailResponse?.location ?: "-"
            )
        )
        list.add(GeneralInfoModel(getString(R.string.email), userDetailResponse?.email ?: "-"))
        list.add(GeneralInfoModel(getString(R.string.bio), userDetailResponse?.bio ?: "-"))
        list.add(
            GeneralInfoModel(
                getString(R.string.twitter_username),
                userDetailResponse?.twitter_username ?: "-"
            )
        )
        list.add(
            GeneralInfoModel(
                getString(R.string.followers),
                userDetailResponse?.followers.toString()
            )
        )
        list.add(
            GeneralInfoModel(
                getString(R.string.following),
                userDetailResponse?.following.toString()
            )
        )

        list.add(
            GeneralInfoModel(
                getString(R.string.created_at),
                userDetailResponse?.created_at?.toDate().toString()
            )
        )

        if (binding.rvUserGeneralInfo.adapter == null) {
            binding.rvUserGeneralInfo.adapter = GeneralInfoAdapter().apply {
                submitList(list)
            }
        } else {
            (binding.rvUserGeneralInfo.adapter as GeneralInfoAdapter).submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.rvUserGeneralInfo.adapter = null
    }
}