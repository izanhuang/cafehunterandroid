package com.izanhuang.cafe_hunter_android.core.ui.components

import ReviewCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.izanhuang.cafe_hunter_android.BuildConfig
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.ReviewViewModel

@Composable
fun CafeDetails(
    place: PlaceResult,
    updateShowReviewForm: (Boolean) -> Unit,
    padding: PaddingValues,
    reviewViewModel: ReviewViewModel
) {
    val reviews by reviewViewModel.cafeReviews.collectAsState()

    LaunchedEffect(place.place_id) {
        reviewViewModel.loadReviews(place.place_id)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
    ) {
        item {
            CafeDetailsHeader(place)
        }

        item {
            Button(
                onClick = { updateShowReviewForm(true) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Write a Review")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Recent Reviews",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        reviews.forEach { review ->
            item { ReviewCard(review) }

        }
    }
}

@Composable
fun CafeDetailsHeader(place: PlaceResult) {
    var showPhotoViewer by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Image Preview
        if (place.photos.isNotEmpty()) {
            val photoUrls = place.photos.map {
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=800&photoreference=${it.photo_reference}&key=${BuildConfig.MAPS_PLACES_API_KEY}"
            }

            AsyncImage(
                model = photoUrls.first(),
                contentDescription = "Cafe photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showPhotoViewer = true },
                contentScale = ContentScale.Crop
            )

            if (showPhotoViewer) {
                CafePhotoViewer(
                    photoUrls = photoUrls,
                    onClose = { showPhotoViewer = false }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = place.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Address
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Address",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = place.vicinity,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Open Now Status
        place.opening_hours?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (it.open_now) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = "Open Now",
                    tint = if (it.open_now) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (it.open_now) "Open Now" else "Closed",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}