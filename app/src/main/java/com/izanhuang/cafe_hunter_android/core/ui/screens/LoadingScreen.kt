package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.izanhuang.cafe_hunter_android.R

@Composable
fun LoadingScreen() {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val dots by infiniteTransition.animateValue(
        initialValue = "",
        targetValue = "...",
        typeConverter = TwoWayConverter(
            convertToVector = { AnimationVector1D(it.length.toFloat()) },
            convertFromVector = { ".".repeat(it.value.toInt()) }
        ),
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1500
                "" at 0
                "." at 500
                ".." at 1000
                "..." at 1500
            },
            repeatMode = RepeatMode.Restart
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.coffee_cup), // Replace with actual image
            contentDescription = "Cafe Logo",
            modifier = Modifier
                .size(180.dp)
                .graphicsLayer(scaleX = scale, scaleY = scale)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Cafe Hunter is scouting cozy spots$dots",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF5D4037), // Coffee brown
            fontWeight = FontWeight.Medium
        )
    }
}
