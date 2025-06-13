package com.izanhuang.cafe_hunter_android.core.domain

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.data.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReviewViewModel(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) : ViewModel() {

    private val _reviewSubmissionState = mutableStateOf(false)
    val reviewSubmissionState: State<Boolean> = _reviewSubmissionState

    private val _cafeReviews = MutableStateFlow<List<Review>>(emptyList())
    val cafeReviews: StateFlow<List<Review>> = _cafeReviews.asStateFlow()

    fun submitReview(place: PlaceResult, review: Review, userId: String) {
        val cafesRef = db.collection("cafes")

        cafesRef.document(place.place_id).get()
            .addOnSuccessListener { docSnapshot ->
                if (docSnapshot.exists()) {
                    // Cafe exists, upload review
                    uploadReview(place.place_id, review, userId)
                } else {
                    val newCafeDoc = cafesRef.document(place.place_id)
                    val newCafe = mapOf(
                        "address" to place.vicinity,
                        "createdAt" to Timestamp.now(),
                        "geopoint" to GeoPoint(place.geometry.location.lat, place.geometry.location.lng),
                        "name" to place.name,
                        "photos" to place.photos
                    )
                    newCafeDoc.set(newCafe)
                        .addOnSuccessListener {
                            uploadReview(place.place_id, review, userId)
                        }
                        .addOnFailureListener {
                            _reviewSubmissionState.value = false
                        }
                }
            }
            .addOnFailureListener {
                _reviewSubmissionState.value = false
            }
    }

    private fun uploadReview(cafeId: String, review: Review, userId: String) {
        val reviewRef = db.collection("cafes")
            .document(cafeId)
            .collection("reviews")
            .document()

        val reviewData = mapOf(
            "coffee_rating" to review.coffeeRating,
            "food_rating" to review.foodRating,
            "space_rating" to review.spaceRating,
            "loudness" to review.loudness,
            "rating" to review.rating,
            "is_busy" to review.isBusy,
            "is_cozy" to review.isCozy,
            "is_work_friendly" to review.isWorkFriendly,
            "would_recommend" to review.wouldRecommend,
            "description" to review.description,
            "user_id" to db.collection("users").document(userId),
            "cafe_id" to db.collection("cafes").document(cafeId)
        )

        reviewRef.set(reviewData).addOnSuccessListener {
            _reviewSubmissionState.value = true
            loadReviews(cafeId) // refresh after submission
        }.addOnFailureListener {
            _reviewSubmissionState.value = false
        }
    }

    fun loadReviews(cafeId: String) {
        db.collection("cafes")
            .document(cafeId)
            .collection("reviews")
            .get()
            .addOnSuccessListener { snapshot ->
                val reviews = snapshot.documents.mapNotNull { doc ->
                    doc.toReview()
                }
                _cafeReviews.value = reviews
            }
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toReview(): Review? {
        return try {
            Review(
                coffeeRating = getLong("coffee_rating")?.toInt() ?: 0,
                foodRating = getLong("food_rating")?.toInt() ?: 0,
                spaceRating = getLong("space_rating")?.toInt() ?: 0,
                loudness = getLong("loudness")?.toInt() ?: 0,
                rating = getLong("rating")?.toInt() ?: 0,
                isBusy = getBoolean("is_busy") ?: false,
                isCozy = getBoolean("is_cozy") ?: false,
                isWorkFriendly = getBoolean("is_work_friendly") ?: false,
                wouldRecommend = getBoolean("would_recommend") ?: false,
                description = getString("description") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }
}
