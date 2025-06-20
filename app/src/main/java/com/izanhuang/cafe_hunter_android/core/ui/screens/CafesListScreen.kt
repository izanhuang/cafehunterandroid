package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.ui.components.CafeCard

@Composable
fun CafesListScreen(cafes: List<PlaceResult>) {
    val listState = rememberLazyListState()
    var selectedCafe by remember { mutableStateOf<PlaceResult?>(null) }

    if (selectedCafe == null) {
        LazyColumn(state = listState) {
            items(cafes) { cafe ->
                CafeCard(place = cafe, onClick = { selectedCafe = cafe })
            }
        }
    } else {
        CafeDetailScreen(place = selectedCafe!!) {
            selectedCafe = null
        }
    }
}