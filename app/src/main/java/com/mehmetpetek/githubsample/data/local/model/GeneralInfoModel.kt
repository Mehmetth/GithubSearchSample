package com.mehmetpetek.githubsample.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeneralInfoModel(
    val title: String,
    val desc: String
) : Parcelable
