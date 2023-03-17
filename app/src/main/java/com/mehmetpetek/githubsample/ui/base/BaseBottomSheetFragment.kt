package com.mehmetpetek.githubsample.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mehmetpetek.githubsample.NavGraphDirections
import com.mehmetpetek.githubsample.R
import com.mehmetpetek.githubsample.common.Constant
import com.mehmetpetek.githubsample.common.util.UiUtil
import com.mehmetpetek.githubsample.ui.common.AutoClearedFragmentValue

abstract class BaseBottomSheetFragment<T : ViewBinding>(
    private val inflate: Inflate<T>,
) : BottomSheetDialogFragment() {

    protected var binding: T by AutoClearedFragmentValue()

    open val canDraggable = false
    abstract fun bindScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindScreen()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                (requireDialog().findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.apply {
                isDraggable = canDraggable
                isHideable = false
                state = BottomSheetBehavior.STATE_EXPANDED
                maxHeight = (UiUtil.getScreenHeight() * 0.9).toInt()
                peekHeight = RelativeLayout.LayoutParams.WRAP_CONTENT
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (slideOffset < 0.01f && state == BottomSheetBehavior.STATE_SETTLING) {
                            dismiss()
                        }
                    }
                })
            }

        }
        return bottomSheetDialog
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
                title ?: getString(R.string.error),
                desc ?: getString(R.string.explanation),
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
        findNavController().navigate(directions)
    }
}
