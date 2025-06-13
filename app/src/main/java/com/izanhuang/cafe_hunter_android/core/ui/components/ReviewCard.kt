import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izanhuang.cafe_hunter_android.core.data.Review
import com.izanhuang.cafe_hunter_android.core.utils.getCustomRelativeTimeString

@Composable
fun ReviewCard(review: Review) {
    val initials = "${review.firstName.firstOrNull()?.uppercase() ?: ""}${review.lastName.firstOrNull()?.uppercase() ?: ""}"
    val relativeTime = getCustomRelativeTimeString(review.created_at)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: Profile Icon + Name + Time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE0E0E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${review.firstName} ${review.lastName}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = relativeTime,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Emoji Ratings
            Text("☕ Coffee: ${review.coffeeRating}/5")
            Text("🍽️ Food: ${review.foodRating}/5")
            Text("🪑 Space: ${review.spaceRating}/5")
            Text("🔊 Loudness: ${review.loudness}/5")
            Text("⭐ Overall: ${review.rating}/5", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            if (review.description.isNotBlank()) {
                Text(
                    text = "\"${review.description}\"",
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tags
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (review.isCozy) ReviewTag("Cozy")
                if (review.isBusy) ReviewTag("Busy")
                if (review.isWorkFriendly) ReviewTag("Work Friendly")
                if (review.wouldRecommend) ReviewTag("Would Recommend")
            }
        }
    }
}

@Composable
fun ReviewTag(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFEDE7F6), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF5E35B1),
            fontWeight = FontWeight.Medium
        )
    }
}
