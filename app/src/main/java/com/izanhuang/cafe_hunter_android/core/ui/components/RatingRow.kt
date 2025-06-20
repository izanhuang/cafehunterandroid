package com.izanhuang.cafe_hunter_android.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconRatingRow(
    rating: MutableState<Int>,
    icon: ImageVector,
    label: String
) {
    Column {
        Text(text = label)
        Row {
            for (i in 1..5) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (i <= rating.value) Color(0xFFB47C4B) else Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { rating.value = if (rating.value == i) 0 else i }
                )
            }
        }
    }
}
