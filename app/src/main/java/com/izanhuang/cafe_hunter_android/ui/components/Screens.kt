package com.izanhuang.cafe_hunter_android.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.izanhuang.cafe_hunter_android.R
import androidx.core.graphics.createBitmap
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon on the screen
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "home",
            tint = Color(0xFF0F9D58)
        )
        // Text on the screen
        Text(text = "Home", color = Color.Black)
    }
}

@Composable
fun SearchScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon on the screen
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search",
            tint = Color(0xFF0F9D58)
        )
        // Text on the screen
        Text(text = "Search", color = Color.Black)
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon on the screen
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color(0xFF0F9D58)
        )
        // Text on the screen
        Text(text = "Profile", color = Color.Black)
    }
}

//@Composable
//fun MapScreen() {
//    val atasehir = LatLng(40.9971, 29.1007)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(atasehir, 15f)
//    }
//
//    var uiSettings by remember {
//        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
//    }
//    var properties by remember {
//        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
//    }
//
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState,
//        properties = properties,
//        uiSettings = uiSettings
//    ) {
//        MarkerInfoWindow(
//            state = MarkerState(position = atasehir),
//            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_foreground)
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.border(
//                        BorderStroke(1.dp, Color.Black),
//                        RoundedCornerShape(10)
//                    )
//                    .clip(RoundedCornerShape(10))
//                    .background(Color.Blue)
//                    .padding(20.dp)
//            ) {
//                Text("Title", fontWeight = FontWeight.Bold, color = Color.White)
//                Text("Description", fontWeight = FontWeight.Medium, color = Color.White)
//            }
//        }
//    }
//}

@Composable
fun MapScreen() {
    val atasehir = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(atasehir, 15f)
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    fun vectorToBitmap(context: Context, @DrawableRes drawableRes: Int): Bitmap {
        val drawable = AppCompatResources.getDrawable(context, drawableRes) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        drawable.setBounds(0, 0, width, height)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)

        return bitmap
    }


    val context = LocalContext.current

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        val size = 80 // px, adjust as needed

        // Create a bitmap and canvas
        val circleBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(circleBitmap)

        // Draw a colored circle
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.parseColor("#BA8787")
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // Draw the cafe icon centered
        val iconDrawable = AppCompatResources.getDrawable(context, R.drawable.baseline_local_cafe_24)
        val iconSize = (size * 0.6).toInt() // 60% of circle size
        val left = (size - iconSize) / 2
        val top = (size - iconSize) / 2
        iconDrawable?.setBounds(left, top, left + iconSize, top + iconSize)
        iconDrawable?.draw(canvas)

        val markerIcon = BitmapDescriptorFactory.fromBitmap(circleBitmap)

        MarkerInfoWindow(
            state = MarkerState(position = atasehir),
            icon = markerIcon,
            title = "Cafe Hunter",
            snippet = "Tap for more"
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, Color.Black),
                        RoundedCornerShape(10)
                    )
                    .clip(RoundedCornerShape(10))
                    .background(Color.Blue)
                    .padding(20.dp)
            ) {
                Text("Title", fontWeight = FontWeight.Bold, color = Color.White)
                Text("Description", fontWeight = FontWeight.Medium, color = Color.White)
            }
        }
//        MarkerComposable(
//            state = MarkerState(position = atasehir),
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.baseline_local_cafe_24),
//                contentDescription = "",
//                modifier = Modifier.padding(16.dp).background(color = Color.White).clip(CircleShape)
//            )
//        }
    }
}