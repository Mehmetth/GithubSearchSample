package com.mehmetpetek.githubsample.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mehmetpetek.githubsample.NavGraphDirections
import com.mehmetpetek.githubsample.R
import com.mehmetpetek.githubsample.common.Constant
import com.mehmetpetek.githubsample.common.extensions.gone
import com.mehmetpetek.githubsample.common.extensions.navigateSafe
import com.mehmetpetek.githubsample.common.extensions.visible
import com.mehmetpetek.githubsample.common.getLoading
import com.mehmetpetek.githubsample.common.showFullPagePopup
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseFragment<T : ViewBinding>(
    private val inflate: Inflate<T>,
) : Fragment() {

    private var progressDialog: Dialog? = null
    private var progress: View? = null
    protected lateinit var binding: T
    open val saveBinding: Boolean = false
    open val isBlackTheme: Boolean = true
    abstract fun bindScreen()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (this::binding.isInitialized && saveBinding) {
            binding
        } else {
            binding = inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindScreen()
    }


    fun showInfo(
        iconId: Int = R.drawable.ic_info,
        title: String? = null,
        desc: String? = null,
        btnPositive: String? = null,
        btnNegative: String? = null,
        cancellable: Boolean = false,
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null,
        dismissCallback: (() -> Unit)? = null,
    ) {
        val directions =
            NavGraphDirections.globalToInfo(
                title ?: getString(R.string.general_error),
                desc ?: getString(R.string.error),
                btnNegative,
                iconId
            ).setButtonPrimary(btnPositive ?: getString(R.string.ok))
                .setCancellable(cancellable)
        setFragmentResultListener(Constant.Args.INFO_PAGE) { _, bundle ->
            if (bundle.getString(Constant.Args.BUTTON) == Constant.Args.BUTTON_POSITIVE) {
                positiveCallback?.invoke()
            }
            if (bundle.getString(Constant.Args.BUTTON) == Constant.Args.BUTTON_NEGATIVE) {
                negativeCallback?.invoke()
            }
            if (bundle.getString(Constant.Args.BUTTON) == Constant.Args.BUTTON_DISMISS) {
                dismissCallback?.invoke()
            }
        }
        findNavController().navigateSafe(directions)
    }

    fun onBackClicked() {
        hideLoading()
        if (!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }

    fun handleError(throwable: Throwable?, primaryListener: (() -> Unit)? = null) {
        when (throwable) {
            is ConnectException,
            is UnknownHostException,
            -> {
                showFullPagePopup(
                    R.drawable.ic_info,
                    title = getString(R.string.error),
                    desc = getString(R.string.no_internet),
                    buttonPrimary = getString(R.string.ok),
                    primaryListener = primaryListener
                )
            }
            else -> {
                showInfo(desc = throwable?.localizedMessage, positiveCallback = {
                    primaryListener?.invoke()
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideLoading()
    }

    fun setLoadingState(
        visible: Boolean,
    ) {
        if (visible) showLoading() else hideLoading()
    }

    fun setLockLoadingState(
        visible: Boolean,
        cancellable: Boolean = false,
        cancelListener: (() -> Unit)? = null,
    ) {
        if (visible) showLockLoading(cancellable, cancelListener) else hideLoading()
    }

    private fun showLockLoading(
        cancellable: Boolean = false,
        cancelListener: (() -> Unit)? = null,
    ) {
        if (progressDialog == null) {
            val c = {
                onBackClicked()
            }
            progressDialog = getLoading(
                cancellable,
                cancelListener ?: kotlin.run { c })
        }
        progressDialog?.show()
    }

    private fun showLoading() {
        if (progress == null) {
            progress = layoutInflater.inflate(R.layout.dialog_progress, null)
            val layoutParams = ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            progress?.layoutParams = layoutParams
            (binding.root as ViewGroup).addView(progress)
        }
        progress?.visible()
    }

    fun isLoadingVisible(): Boolean {
        return progress?.visibility == View.VISIBLE || progressDialog?.isShowing == true
    }

    private fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
        progress?.gone()
        progress = null
    }

}