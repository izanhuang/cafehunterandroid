package com.izanhuang.cafe_hunter_android.core.data

data class ReviewWithUser(
    val review: Review,
    val userFirstName: String? = null,
    val userLastName: String? = null
)