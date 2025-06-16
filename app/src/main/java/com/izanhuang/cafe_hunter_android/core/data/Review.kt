package com.izanhuang.cafe_hunter_android.core.data

import com.google.firebase.Timestamp

data class Review(
    val coffeeRating: Int = 0,
    val foodRating: Int = 0,
    val spaceRating: Int = 0,
    val loudness: Int = 0,
    val rating: Int = 0,
    val isBusy: Boolean = false,
    val isCozy: Boolean = false,
    val isWorkFriendly: Boolean = false,
    val wouldRecommend: Boolean = false,
    val description: String = "",
    val cafe_id: String = "",
    val user_id: String = "",
    val created_at: Timestamp
)