package com.izanhuang.cafe_hunter_android.core.utils

import androidx.compose.runtime.Composable

@Composable
fun <T1, T2, R> safeLet(p1: T1?, p2: T2?, block: @Composable (T1, T2) -> R): R? {
    return if (p1 != null && p2 != null) {
        block(p1, p2)
    } else {
        null
    }
}