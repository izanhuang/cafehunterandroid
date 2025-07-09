package com.izanhuang.cafe_hunter_android.core.ui.components.images

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.izanhuang.cafe_hunter_android.core.ui.components.modifiers.shimmerEffect

@Composable
fun PhotoGallery(photoUrls: List<String>) {
    var isGalleryOpen by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        photoUrls.forEachIndexed { index, url ->
            var imageLoading by remember { mutableStateOf(true) }

            AsyncImage(
                model = url,
                contentDescription = "Review Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                    .clickable {
                        selectedIndex = index
                        isGalleryOpen = true
                    }
                    .then(if (imageLoading) Modifier.shimmerEffect() else Modifier),
                onSuccess = { imageLoading = false },
                onError = { imageLoading = false }
            )
        }
    }

    if (isGalleryOpen) {
        FullscreenPhotoCarousel(
            photoUrls = photoUrls,
            startIndex = selectedIndex,
            onDismiss = { isGalleryOpen = false }
        )
    }
}