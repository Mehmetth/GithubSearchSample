package com.mehmetpetek.githubsample.common.extensions

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun NavController.navigateSafe(
    directions: NavDirections
) {
    val action =
        currentDestination?.getAction(directions.actionId) ?: graph.getAction(directions.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(directions)
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}