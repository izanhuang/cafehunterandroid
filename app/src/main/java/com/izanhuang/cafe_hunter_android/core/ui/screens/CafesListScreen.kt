package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.izanhuang.cafe_hunter_android.BuildConfig
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult

@Composable
fun CafeCard(place: PlaceResult, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Load photo if available
            place.photos?.firstOrNull()?.let { photo ->
                val photoUrl = "https://maps.googleapis.com/maps/api/place/photo" +
                        "?maxwidth=400&photoreference=${photo.photo_reference}&key=${BuildConfig.MAPS_PLACES_API_KEY}"
                AsyncImage(
                    model = photoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = place.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            place.rating?.let {
                Text(
                    text = "⭐ $it (${place.user_ratings_total ?: 0} reviews)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Text(text = place.vicinity, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            place.opening_hours?.let {
                val openStatus = if (it.open_now) "Open now" else "Closed"
                Text(
                    text = openStatus,
                    color = if (it.open_now) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun CafesListScreen(cafes: List<PlaceResult>) {
    LazyColumn {
        items(cafes) { cafe ->
            CafeCard(cafe)
        }
    }
}