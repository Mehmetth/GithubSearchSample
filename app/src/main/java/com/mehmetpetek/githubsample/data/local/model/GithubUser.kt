package com.mehmetpetek.githubsample.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "GithubUser")
data class GithubUser(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Int
) : Parcelable
