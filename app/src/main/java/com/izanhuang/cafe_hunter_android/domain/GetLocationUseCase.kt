package com.izanhuang.cafe_hunter_android.domain
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import com.google.android.gms.maps.model.LatLng
//import com.izanhuang.cafe_hunter_android.features.location.services.ILocationService
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class GetLocationUseCase @Inject constructor(
//    private val locationService: ILocationService
//) {
//    @RequiresApi(Build.VERSION_CODES.S)
//    operator fun invoke(): Flow<LatLng?> = locationService.requestLocationUpdates()
//
//}