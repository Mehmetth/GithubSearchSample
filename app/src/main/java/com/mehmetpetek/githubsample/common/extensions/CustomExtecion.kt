package com.mehmetpetek.githubsample.common

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.mehmetpetek.githubsample.R
import com.mehmetpetek.githubsample.databinding.DialogFullPopupBinding

fun Fragment.resColor(@ColorRes colorRes: Int) =
    ResourcesCompat.getColor(resources, colorRes, null)

fun Fragment.showFullPagePopup(
    @DrawableRes iconId: Int,
    title: String? = null,
    desc: String? = null,
    buttonPrimary: String? = null,
    isCancellable: Boolean = false,
    primaryListener: (() -> Unit)? = null,
    cancelListener: (() -> Unit)? = null,
) {
    val dialog = Dialog(requireContext(), R.style.BaseDialogStyle)
    val binding: DialogFullPopupBinding =
        DialogFullPopupBinding.inflate(dialog.layoutInflater, null, false)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    dialog.window?.statusBarColor = resColor(android.R.color.white)
    binding.ivIcon.setImageResource(iconId)
    binding.tvTitle.text = title
    binding.tvDescription.text = desc
    binding.btnPrimary.text = buttonPrimary
    dialog.setCancelable(isCancellable)
    dialog.setCanceledOnTouchOutside(false)
    binding.btnPrimary.setOnClickListener {
        dialog.dismiss()
        primaryListener?.invoke()
    }
    dialog.setOnCancelListener {
        cancelListener?.invoke()
    }
    dialog.show()
}

fun Fragment.getLoading(cancellable: Boolean, cancelListener: (() -> Unit)?): Dialog {
    val dialogView = Dialog(requireContext())
    dialogView.window?.requestFeature(Window.FEATURE_NO_TITLE)
    dialogView.setContentView(R.layout.component_loading)
    dialogView.window?.setDimAmount(0.1f)
    dialogView.window?.setBackgroundDrawable(null)
    dialogView.setCancelable(cancellable)
    dialogView.setOnCancelListener { cancelListener?.invoke() }
    dialogView.setCanceledOnTouchOutside(false)
    return dialogView
}