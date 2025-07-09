package com.izanhuang.cafe_hunter_android.core.ui.components.images

import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage

@Composable
fun ZoomableImage(
    url: String,
    onDismiss: () -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var dragAmountY by remember { mutableStateOf(0f) }
    var isZoomed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { containerSize = it }  // Capture container size
            .clipToBounds()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val zoomEvent = event.calculateZoom()
                        val panEvent = event.calculatePan()

                        if (zoomEvent != 1f) {
                            val newScale = (scale * zoomEvent).coerceIn(1f, 5f)
                            scale = newScale
                            isZoomed = scale > 1f
                            if (scale == 1f) offset = Offset.Zero
                        } else if (panEvent != Offset.Zero) {
                            if (isZoomed) {
                                val maxX = (containerSize.width * (scale - 1)) / 2f
                                val maxY = (containerSize.height * (scale - 1)) / 2f

                                // Apply pan and clamp within bounds
                                val newOffsetX = (offset.x + panEvent.x).coerceIn(-maxX, maxX)
                                val newOffsetY = (offset.y + panEvent.y).coerceIn(-maxY, maxY)
                                offset = Offset(newOffsetX, newOffsetY)

                                event.changes.forEach { it.consume() }
                            } else {
                                val verticalDrag = panEvent.y
                                dragAmountY += verticalDrag
                                if (kotlin.math.abs(dragAmountY) > 150f) {
                                    onDismiss()
                                    dragAmountY = 0f
                                }

                                if (verticalDrag != 0f) {
                                    event.changes.forEach { pointerInputChange ->
                                        if (kotlin.math.abs(pointerInputChange.positionChange().y) > kotlin.math.abs(pointerInputChange.positionChange().x)) {
                                            pointerInputChange.consume()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                            isZoomed = false
                        } else {
                            scale = 2.5f
                            isZoomed = true
                        }
                    }
                )
            }
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y + dragAmountY
                scaleX = scale
                scaleY = scale
            }
    ) {
        AsyncImage(
            model = url,
            contentDescription = "Zoomable photo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}