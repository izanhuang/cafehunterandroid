package com.izanhuang.cafe_hunter_android.core.data

import com.google.firebase.Timestamp

data class User(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: String = "",
    val birthday: String = "",
    val createdAt: Timestamp? = null
)
