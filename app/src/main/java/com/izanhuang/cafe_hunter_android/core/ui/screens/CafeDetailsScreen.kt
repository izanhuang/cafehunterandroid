package com.izanhuang.cafe_hunter_android.core.ui.screens

import ReviewForm
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.domain.ReviewViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.cafes.CafeDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeDetailScreen(
    place: PlaceResult,
    navController: NavController,
    onBack: () -> Unit
) {
    val reviewViewModel = remember { ReviewViewModel() }
    var showReviewForm by remember { mutableStateOf(false) }
    var hasLaunchedOnce by remember { mutableStateOf(false) }

    val submissionState by reviewViewModel.reviewSubmissionState
    val context = LocalContext.current

    // Show toast on successful submission
    LaunchedEffect(submissionState) {
        if (hasLaunchedOnce && !submissionState) {
            Toast.makeText(context, "Review submitted!", Toast.LENGTH_SHORT).show()
            showReviewForm = false
        } else {
            hasLaunchedOnce = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(place.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                windowInsets = WindowInsets(top = 0.dp)
            )
        }
    ) { padding ->
        if (!showReviewForm) {
            CafeDetails(
                place = place,
                updateShowReviewForm = { showReviewForm = it },
                padding = padding,
                reviewViewModel = reviewViewModel,
                navController = navController
            )
        } else {
            ReviewForm(
                place = place,
                reviewViewModel = reviewViewModel
            )
        }
    }
}
