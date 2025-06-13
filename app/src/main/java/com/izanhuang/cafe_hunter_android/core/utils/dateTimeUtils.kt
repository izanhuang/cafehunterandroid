package com.izanhuang.cafe_hunter_android.core.utils

import android.text.format.DateUtils

fun getCustomRelativeTimeString(timeMillis: Long): String {
    return DateUtils.getRelativeTimeSpanString(
        timeMillis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}