package com.izanhuang.cafe_hunter_android.core.ui.components

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

@Composable
fun setCoffeeCupMapIconWithText(
    message: String,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    containerColor: Color = MaterialTheme.colorScheme.primary
): BitmapDescriptor {
    val paintStokeWidth = 4.dp.value

    // White outline paint
    val paintTextOutline = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.WHITE // Outline color
        textAlign = Paint.Align.LEFT
        style = Paint.Style.STROKE
        strokeWidth = 6f // Thickness of the outline
        textSize = 36.dp.value
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    // Main text paint
    val paintText = Paint().apply {
        isAntiAlias = true
        color = containerColor.toArgb() // Use toArgb instead of hashCode
        textAlign = Paint.Align.LEFT
        style = Paint.Style.FILL
        textSize = 36.dp.value
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val iconWidth = 80f
    val iconHeight = 100f
    val textPadding = 16f
    val textWidth = paintText.measureText(message)
    val height = iconHeight
    val width = iconWidth + textPadding + textWidth

    val bitmapWidth = (width + paintStokeWidth).toInt()
    val bitmapHeight = (height + paintStokeWidth).toInt()
    val bm = createBitmap(bitmapWidth, bitmapHeight)
    bm.setHasAlpha(true)
    val canvas = Canvas(bm)
    canvas.drawColor(android.graphics.Color.TRANSPARENT)

    // Draw main pin shape
    val markerPaint = Paint().apply {
        color = contentColor.toArgb()
        isAntiAlias = true
        style = Paint.Style.FILL
        setShadowLayer(12f, 0f, 4f, android.graphics.Color.argb(100, 0, 0, 0))
    }
    val centerX = iconWidth / 2f
    val circleRadius = 36f
    val circleCenterY = 40f

    // Pin top exactly at circle bottom edge
    val pinTopY = circleCenterY + (circleRadius / 3)

    val pinLeftX = centerX - 20f
    val pinRightX = centerX + 20f

    val combinedPath = Path().apply {
        // Draw the circle
        addCircle(centerX, circleCenterY, circleRadius, Path.Direction.CW)

        // Start pin top at left
        moveTo(pinLeftX, pinTopY)

        // Slight curve following circle bottom - control point at same Y to avoid dip
        quadTo(centerX, pinTopY, pinRightX, pinTopY)

        // Pin tip
        lineTo(centerX, iconHeight - 5f)

        // Close the path
        close()
    }

    canvas.drawPath(combinedPath, markerPaint)

    // Draw inner circle
    val circlePaint = Paint().apply {
        color = containerColor.toArgb()
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, circleCenterY, 26f, circlePaint)

    // Coffee cup emoji ☕
    val emojiPaint = Paint().apply {
        color = containerColor.toArgb()
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = 34f // Adjust for size within the circle
    }
    canvas.drawText("☕", centerX, circleCenterY + emojiPaint.textSize / 3, emojiPaint)

    // Draw the text to the right of the icon
    val textX = iconWidth + textPadding
    val textY = circleCenterY + paintText.textSize / 3

    // Draw outline behind
    canvas.drawText(message, textX, textY, paintTextOutline)

    // Draw actual text on top
    canvas.drawText(message, textX, textY, paintText)

    return BitmapDescriptorFactory.fromBitmap(bm)
}
