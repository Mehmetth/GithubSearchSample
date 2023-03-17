package com.mehmetpetek.githubsample.common.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide


fun ImageView.setImage(imageUrl: String, scaleType: ScaleType) {
    Glide.with(this.context)
        .load(imageUrl)
        .apply {
            if (scaleType == ScaleType.CENTER_CROP)
                this.centerCrop()
            else
                this.centerInside()

            into(this@setImage)
        }
}

enum class ScaleType {
    CENTER_INSIDE,
    CENTER_CROP
}