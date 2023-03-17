package com.mehmetpetek.githubsample.ui.info

import android.content.DialogInterface
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.mehmetpetek.githubsample.common.Constant
import com.mehmetpetek.githubsample.databinding.FragmentInfoBinding
import com.mehmetpetek.githubsample.ui.base.BaseBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment :
    BaseBottomSheetFragment<FragmentInfoBinding>(FragmentInfoBinding::inflate) {

    private val args: InfoFragmentArgs by navArgs()

    private var isPositiveClicked = false
    override val canDraggable: Boolean = true

    override fun bindScreen() {
        binding.btnPrimary.text = args.buttonPrimary
        binding.tvTitle.text = args.title
        binding.tvDescription.text = args.desc
        isCancelable = args.cancellable
        if (!args.buttonSecondary.isNullOrEmpty()) {
            binding.btnSecondary.visibility = View.VISIBLE
            binding.btnSecondary.text = args.buttonSecondary
        }

        binding.btnPrimary.setOnClickListener {
            isPositiveClicked = true
            dismiss()
        }
        binding.btnSecondary.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (isPositiveClicked) {
            setFragmentResult(
                Constant.Args.INFO_PAGE,
                bundleOf(Constant.Args.BUTTON to Constant.Args.BUTTON_POSITIVE)
            )
        } else {
            setFragmentResult(
                Constant.Args.INFO_PAGE,
                bundleOf(Constant.Args.BUTTON to Constant.Args.BUTTON_NEGATIVE)
            )
        }

    }
}