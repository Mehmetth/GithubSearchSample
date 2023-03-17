package com.mehmetpetek.githubsample.ui.home

import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mehmetpetek.githubsample.R
import com.mehmetpetek.githubsample.common.extensions.gone
import com.mehmetpetek.githubsample.common.extensions.navigateSafe
import com.mehmetpetek.githubsample.common.extensions.visible
import com.mehmetpetek.githubsample.data.remote.model.Users
import com.mehmetpetek.githubsample.databinding.FragmentHomeBinding
import com.mehmetpetek.githubsample.ui.base.BaseFragment
import com.mehmetpetek.githubsample.ui.home.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    UsersAdapter.OnUserListener {

    private val viewModel by viewModels<HomeViewModel>()

    override fun bindScreen() {
        setSearchView()

        lifecycleScope.launchWhenResumed {
            viewModel.effect.collect {
                when (it) {
                    is HomeEffect.ShowError -> {
                        handleError(it.throwable)
                    }
                    is HomeEffect.GoToUserDetail -> {
                        findNavController().navigateSafe(
                            HomeFragmentDirections.homeFragmentToUserDetailFragment(it.login)
                        )
                    }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.state.collect {
                setLoadingState(it.isLoading)

                setTotalCount(it.totalCount ?: 0)
                setList(it.usersList, it.changeIconIndex)
            }
        }
    }

    private fun setSearchView() {
        binding.svUsername.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                if (msg.isEmpty()) {
                    setMessage(getString(R.string.search_pls))

                    binding.rvUsers.adapter = null
                    binding.rvUsers.gone()
                } else if (msg.length > 2) {
                    viewModel.setEvent(
                        HomeEvent.SearchUser(
                            msg
                        )
                    )
                }
                return false
            }
        })
    }

    private fun setTotalCount(count: Int) {
        binding.tvResultCount.text = "${getString(R.string.found_users_count)} $count"
    }

    private fun setList(userList: List<Users>?, index: Int) {
        userList?.let {
            binding.rvUsers.visible()
            binding.tvNotFound.gone()
            binding.tvResultCount.visible()

            binding.rvUsers.setHasFixedSize(true)
            if (binding.rvUsers.adapter == null) {
                val adapter = UsersAdapter(this).apply {
                    submitList(userList)
                }
                adapter.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                binding.rvUsers.adapter = adapter
                binding.rvUsers.adapter = adapter
            } else {
                (binding.rvUsers.adapter as UsersAdapter).notifyItemChanged(index)
                (binding.rvUsers.adapter as UsersAdapter).submitList(userList)
            }

            if (it.isEmpty()) {
                setMessage(getString(R.string.not_found))
            }
        } ?: kotlin.run {
            setMessage(getString(R.string.search_pls))
        }
    }

    private fun setMessage(message: String) {
        binding.tvNotFound.text = message
        binding.tvNotFound.visible()
        binding.tvResultCount.gone()
    }

    override fun onSelectUserClicked(login: String) {
        viewModel.setEvent(HomeEvent.GoToUserDetail(login))
    }

    override fun onSelectFavoriteClicked(index: Int, id: Int) {
        viewModel.setEvent(HomeEvent.ClickFavorite(index, id))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.rvUsers.adapter = null
    }
}