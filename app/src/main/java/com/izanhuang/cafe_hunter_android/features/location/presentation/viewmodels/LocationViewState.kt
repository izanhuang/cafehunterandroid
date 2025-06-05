package com.izanhuang.cafe_hunter_android.features.location.presentation.viewmodels
//
//import com.google.android.gms.maps.model.LatLng
//
//sealed interface ViewState {
//    data object Loading : ViewState
//    data class Success(val location: LatLng?) : ViewState
//    data object RevokedPermissions : ViewState
//}
//
//sealed interface PermissionEvent {
//    data object Granted : PermissionEvent
//    data object Revoked : PermissionEvent
//}