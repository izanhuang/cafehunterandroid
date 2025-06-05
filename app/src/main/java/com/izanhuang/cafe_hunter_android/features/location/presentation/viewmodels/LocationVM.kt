package com.izanhuang.cafe_hunter_android.features.location.presentation.viewmodels
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.izanhuang.cafe_hunter_android.domain.GetLocationUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@RequiresApi(Build.VERSION_CODES.S)
//@HiltViewModel
//class LocationVM @Inject constructor(
//    private val getLocationUseCase: GetLocationUseCase
//) : ViewModel() {
//
//    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
//    val viewState = _viewState.asStateFlow()
//
//    /* This function is responsible for updating the ViewState based
//       on the event coming from the view */
//    fun handle(event: PermissionEvent) {
//        when (event) {
//            PermissionEvent.Granted -> {
//                viewModelScope.launch {
//                    getLocationUseCase.invoke().collect { location ->
//                        _viewState.value = ViewState.Success(location)
//                    }
//                }
//            }
//
//            PermissionEvent.Revoked -> {
//                _viewState.value = ViewState.RevokedPermissions
//            }
//        }
//    }
//}