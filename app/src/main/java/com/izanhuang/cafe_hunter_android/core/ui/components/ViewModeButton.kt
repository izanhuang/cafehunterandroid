package com.izanhuang.cafe_hunter_android.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.izanhuang.cafe_hunter_android.R

@Composable
fun ViewModeButton(isMapView: Boolean, updateIsViewMode: () -> Unit) {
    Button(
        onClick = { updateIsViewMode() },
        colors = ButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimary),
        modifier = Modifier
            .padding(8.dp)
            .height(48.dp)
    ) {
        val viewIcon = if (isMapView) {
            R.drawable.round_view_list_24
        } else {
            R.drawable.round_map_24
        }
        val viewDescription = if (isMapView) {
            "List view"
        } else {
            "Map view"
        }
        Icon(
            painter = painterResource(viewIcon),
            contentDescription = viewDescription
        )
    }
}