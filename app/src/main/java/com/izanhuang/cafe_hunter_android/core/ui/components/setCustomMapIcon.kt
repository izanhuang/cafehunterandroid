package com.izanhuang.cafe_hunter_android.core.ui.components

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

@Composable
fun setCustomMapIcon(
    message: String,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary
): BitmapDescriptor {
    val paintStokeWidth = 4.dp.value

    val paintFill = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = containerColor.hashCode()
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 30.dp.value
    }

    val paintText = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = contentColor.hashCode()
        textAlign = Paint.Align.CENTER
        strokeWidth = paintStokeWidth
        textSize = 36.dp.value
    }

    val paintOutline = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = contentColor.hashCode()
        strokeWidth = paintStokeWidth
    }

    val height = 100f
    val widthPadding = 80.dp.value
    val width = paintText.measureText(message, 0, message.length) + widthPadding

    // Add padding to bitmap size to avoid clipping the stroke
    val bitmapWidth = (width + paintStokeWidth).toInt()
    val bitmapHeight = (height + paintStokeWidth).toInt()

    val roundStart = height / 3
    val path = Path().apply {
        // Offset everything by strokeWidth / 2 to fit inside bitmap
        val offset = paintStokeWidth / 2
        arcTo(
            offset, offset,
            offset + roundStart * 2, offset + roundStart * 2,
            -90f, -180f, true
        )
        lineTo(offset + width / 2 - roundStart / 2, offset + height * 2 / 3)
        lineTo(offset + width / 2, offset + height)
        lineTo(offset + width / 2 + roundStart / 2, offset + height * 2 / 3)
        lineTo(offset + width - roundStart, offset + height * 2 / 3)
        arcTo(
            offset + width - roundStart * 2, offset,
            offset + width, offset + height * 2 / 3,
            90f, -180f, true
        )
        lineTo(offset + roundStart, offset)
    }

    val bm = createBitmap(bitmapWidth, bitmapHeight)
    val canvas = Canvas(bm)
    canvas.drawPath(path, paintFill)
    canvas.drawPath(path, paintOutline)
    canvas.drawText(
        message,
        paintStokeWidth / 2 + width / 2,
        paintStokeWidth / 2 + height * 2 / 3 * 2 / 3,
        paintText
    )
    return BitmapDescriptorFactory.fromBitmap(bm)
}