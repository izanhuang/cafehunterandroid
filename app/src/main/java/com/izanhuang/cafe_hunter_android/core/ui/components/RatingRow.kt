package com.izanhuang.cafe_hunter_android.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                        .clickable { rating.value = i }
                )
            }
        }
    }
}

@Composable
fun RatingRow(label: String, rating: MutableState<Int>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(label)
        Row {
            (1..5).forEach { index ->
                IconButton(onClick = { rating.value = index }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index <= rating.value) Color(0xFF795548) else Color.Gray
                    )
                }
            }
        }
    }
}
