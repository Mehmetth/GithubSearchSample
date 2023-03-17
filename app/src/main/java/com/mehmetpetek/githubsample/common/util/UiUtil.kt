package com.mehmetpetek.githubsample.common.util

import android.content.res.Resources

object UiUtil {
    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}