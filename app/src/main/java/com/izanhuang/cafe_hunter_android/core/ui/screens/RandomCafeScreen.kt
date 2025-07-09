package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.CafesViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.cafes.CafeDetailsHeader
import com.izanhuang.cafe_hunter_android.core.utils.Resource
import kotlin.math.roundToInt

@Composable
fun RandomCafeScreen(cafesViewModel: CafesViewModel) {
    var radius by remember { mutableStateOf(1000f) }

    val mapUiState by cafesViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Cafe Roulette",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        RadiusUseCaseTable()
        Text("Distance (km): ${radius.roundToInt()}")
        Slider(
            value = radius,
            onValueChange = { radius = it },
            valueRange = 100f..10000f,
            steps = 10,
            modifier = Modifier.padding(8.dp)
        )
        Button(
            onClick = { cafesViewModel.getRandomCafe(radius.roundToInt()) }
        ) {
            Text("Surprise Me!", color = MaterialTheme.colorScheme.onPrimary)
        }

        when (val state = mapUiState) {
            is Resource.Success -> if (state.data.randomCafe != null)
                RandomCafeDetails(cafe = state.data.randomCafe)
            else FindCafePlaceholder()

            is Resource.Error -> Text("Error: ${state.message}")
            Resource.Loading -> Text("Loading...")
        }
    }
}

@Composable
fun FindCafePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("No cafe picked yet — tap the button for a surprise!", textAlign = TextAlign.Center)
    }
}

@Composable
fun RadiusUseCaseTable() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Use Case", fontWeight = FontWeight.Bold)
            Text("Suggested Radius", fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        // Table rows
        val rows = listOf(
            "Nearby cafes" to "100 – 1500 meters",
            "Walkable area" to "500 – 2000 meters",
            "Driving distance" to "2000 – 10000 meters"
        )

        rows.forEachIndexed { index, (useCase, radius) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(useCase, modifier = Modifier.weight(1f))
                Text(radius, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

            if (index != rows.lastIndex) {
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
}


@Composable
fun RandomCafeDetails(cafe: PlaceResult) {
    CafeDetailsHeader(place = cafe)
}