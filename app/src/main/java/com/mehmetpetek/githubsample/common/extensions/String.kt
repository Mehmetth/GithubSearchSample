package com.mehmetpetek.githubsample.common.extensions

import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(): String {
    val sourceSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val requiredSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return requiredSdf.format(sourceSdf.parse(this))
}