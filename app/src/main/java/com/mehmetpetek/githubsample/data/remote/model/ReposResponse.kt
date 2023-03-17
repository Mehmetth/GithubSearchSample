package com.mehmetpetek.githubsample.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReposResponse(
    val id: Int,
    val name: String,
    val full_name: String,
    val description: String,
    val stargazers_count: Int,
    val language: String,
    val visibility: String,
    val watchers: Int,
    val default_branch: String
) : Parcelable