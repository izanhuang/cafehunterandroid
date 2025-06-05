package com.izanhuang.cafe_hunter_android.features.location.di
//
//import android.content.Context
//import com.google.android.gms.location.LocationServices
//import com.izanhuang.cafe_hunter_android.features.location.services.ILocationService
//import com.izanhuang.cafe_hunter_android.features.location.services.LocationService
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object LocationModule {
//
//    @Singleton
//    @Provides
//    fun provideLocationClient(
//        @ApplicationContext context: Context
//    ): ILocationService = LocationService(
//        context,
//        LocationServices.getFusedLocationProviderClient(context)
//    )
//}