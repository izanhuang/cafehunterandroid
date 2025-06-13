package com.izanhuang.cafe_hunter_android.core.data

data class Review(
    val coffeeRating: Int,
    val foodRating: Int,
    val spaceRating: Int,
    val loudness: Int,
    val rating: Int,
    val isBusy: Boolean,
    val isCozy: Boolean,
    val isWorkFriendly: Boolean,
    val wouldRecommend: Boolean,
    val description: String
)
