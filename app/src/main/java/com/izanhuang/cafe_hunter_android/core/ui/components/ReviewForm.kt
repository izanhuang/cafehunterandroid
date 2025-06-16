import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.data.Review
import com.izanhuang.cafe_hunter_android.core.domain.ReviewViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.IconRatingRow
import com.izanhuang.cafe_hunter_android.core.ui.components.ToggleRow

@Composable
fun ReviewForm(
    place: PlaceResult,
    reviewViewModel: ReviewViewModel
) {
    val coffeeRating = remember { mutableStateOf(0) }
    val foodRating = remember { mutableStateOf(0) }
    val spaceRating = remember { mutableStateOf(0) }
    val loudnessRating = remember { mutableStateOf(0) }
    val overallRating = remember { mutableStateOf(0) }

    val isBusy = remember { mutableStateOf(false) }
    val isCozy = remember { mutableStateOf(false) }
    val isWorkFriendly = remember { mutableStateOf(false) }
    val wouldRecommend = remember { mutableStateOf(false) }

    val reviewText = remember { mutableStateOf("") }
    val submitting = !reviewViewModel.reviewSubmissionState.value
    val showRatingError = remember { mutableStateOf(false) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { IconRatingRow(coffeeRating, Icons.Default.Star, "Coffee") }
        item { IconRatingRow(foodRating, Icons.Default.Menu, "Food") }
        item { IconRatingRow(spaceRating, Icons.Default.Star, "Space / Vibes") }
        item { IconRatingRow(loudnessRating, Icons.Default.Star, "Loudness") }

        item {
            Column {
                IconRatingRow(overallRating, Icons.Default.Star, "Overall")
                if (showRatingError.value && overallRating.value == 0) {
                    Text(
                        text = "Overall rating is required.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        item { ToggleRow("Busy?", isBusy) }
        item { ToggleRow("Cozy?", isCozy) }
        item { ToggleRow("Work Friendly?", isWorkFriendly) }
        item { ToggleRow("Would Recommend?", wouldRecommend) }

        item {
            OutlinedTextField(
                value = reviewText.value,
                onValueChange = { reviewText.value = it },
                label = { Text("Review Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }

        item {
            Button(
                onClick = {
                    if (overallRating.value == 0) {
                        showRatingError.value = true
                        return@Button
                    }

                    val review = Review(
                        coffeeRating = coffeeRating.value,
                        foodRating = foodRating.value,
                        spaceRating = spaceRating.value,
                        loudness = loudnessRating.value,
                        rating = overallRating.value,
                        isBusy = isBusy.value,
                        isCozy = isCozy.value,
                        isWorkFriendly = isWorkFriendly.value,
                        wouldRecommend = wouldRecommend.value,
                        description = reviewText.value,
                        created_at = Timestamp.now()
                    )

                    showRatingError.value = false
                    reviewViewModel.submitReview(place, review, userId)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = submitting
            ) {
                if (!submitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Review")
                }
            }
        }
    }
}
