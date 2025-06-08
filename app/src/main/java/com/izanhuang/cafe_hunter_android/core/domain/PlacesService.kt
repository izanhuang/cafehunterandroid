package com.izanhuang.cafe_hunter_android.core.domain

import com.izanhuang.cafe_hunter_android.core.data.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesService {
    @GET("place/nearbysearch/json")
    suspend fun getNearbyCafes(
        @Query("location") location: String, // "lat,lng"
        @Query("radius") radius: Int = 1500, // meters
        @Query("type") type: String = "cafe",
        @Query("key") apiKey: String
    ): PlacesResponse
}