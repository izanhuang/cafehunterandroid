import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.izanhuang.cafe_hunter_android.core.data.ReviewWithUser
import com.izanhuang.cafe_hunter_android.core.ui.components.modifiers.shimmerEffect
import com.izanhuang.cafe_hunter_android.core.utils.getCustomRelativeTimeString

@Composable
fun ReviewCard(reviewWithUser: ReviewWithUser) {
    val relativeTime = getCustomRelativeTimeString(reviewWithUser.review.created_at.toDate().time)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: Profile Icon + Name + Time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.secondary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.fillMaxSize(0.8f)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                val reviewName = "${reviewWithUser.userFirstName} ${reviewWithUser.userLastName}"
                Column {
                    Text(
                        text = reviewName.ifBlank { "Anonymous" },
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = relativeTime,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Emoji Ratings
            if (reviewWithUser.review.coffeeRating in 1..5) {
                Text("☕ Coffee: ${reviewWithUser.review.coffeeRating}/5")
            }
            if (reviewWithUser.review.foodRating in 1..5) {
                Text("🍽️ Food: ${reviewWithUser.review.foodRating}/5")
            }
            if (reviewWithUser.review.spaceRating in 1..5) {
                Text("🪑 Space: ${reviewWithUser.review.spaceRating}/5")
            }
            if (reviewWithUser.review.loudness in 1..5) {
                Text("🔊 Loudness: ${reviewWithUser.review.loudness}/5")
            }
            Text("⭐ Overall: ${reviewWithUser.review.rating}/5", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            if (reviewWithUser.review.description.isNotBlank()) {
                Text(
                    text = "\"${reviewWithUser.review.description}\"",
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (reviewWithUser.review.isCozy) ReviewTag("Cozy")
                if (reviewWithUser.review.isBusy) ReviewTag("Busy")
                if (reviewWithUser.review.isWorkFriendly) ReviewTag("Work Friendly")
                if (reviewWithUser.review.wouldRecommend) ReviewTag("Would Recommend")
            }

            // Photo Gallery
            val photoUrls = reviewWithUser.review.photo_urls
            if (photoUrls.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    photoUrls.forEach { url ->
                        var imageLoading by remember { mutableStateOf(true) }

                        AsyncImage(
                            model = url,
                            contentDescription = "Review Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                )
                                .then(if (imageLoading) Modifier.shimmerEffect() else Modifier),
                            onSuccess = { imageLoading = false },
                            onError = { imageLoading = false }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ReviewTag(text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}
