package com.izanhuang.cafe_hunter_android.core.domain

import com.izanhuang.cafe_hunter_android.core.data.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyCafes(
        @Query("location") location: String, // "lat,lng"
//        @Query("radius") radius: Int = 1500, // meters
//        @Query("type") type: String = "cafe",
        @Query("rankby") rankBy: String = "distance",
        @Query("key") apiKey: String,
        @Query("keyword") keyword: String = "cafe|coffee|coffee shop|bakery|espresso|tea|bubble tea|boba|dessert|bakery|milk tea"
    ): PlacesResponse
}