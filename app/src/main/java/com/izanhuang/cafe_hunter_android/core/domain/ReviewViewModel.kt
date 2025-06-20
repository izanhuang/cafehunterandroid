package com.izanhuang.cafe_hunter_android.core.domain

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.izanhuang.cafe_hunter_android.core.data.PlaceResult
import com.izanhuang.cafe_hunter_android.core.data.Review
import com.izanhuang.cafe_hunter_android.core.data.ReviewWithUser
import com.izanhuang.cafe_hunter_android.core.data.User

class ReviewViewModel(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) :
    ViewModel() {

    private val _reviewSubmissionState = mutableStateOf(false)
    val reviewSubmissionState: State<Boolean> = _reviewSubmissionState

    private var lastVisibleReview: DocumentSnapshot? = null
    private val _reviews = mutableStateListOf<ReviewWithUser>()
    val reviews: List<ReviewWithUser> get() = _reviews

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
                        "geopoint" to GeoPoint(
                            place.geometry.location.lat,
                            place.geometry.location.lng
                        ),
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
            "cafe_id" to db.collection("cafes").document(cafeId),
            "created_at" to Timestamp.now(),
        )

        reviewRef.set(reviewData).addOnSuccessListener {
            _reviewSubmissionState.value = true
        }.addOnFailureListener {
            _reviewSubmissionState.value = false
        }
    }

    fun loadReviews(cafeId: String, limit: Long = 10, reset: Boolean = false) {
        if (reset) {
            lastVisibleReview = null
            _reviews.clear()
        }

        var reviewsQuery = db.collection("cafes")
            .document(cafeId)
            .collection("reviews")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(limit)

        lastVisibleReview?.let {
            reviewsQuery = reviewsQuery.startAfter(it)
        }

        reviewsQuery.get()
            .addOnSuccessListener { reviewsSnapshot ->
                if (reviewsSnapshot.isEmpty) return@addOnSuccessListener

                val tempList = mutableListOf<ReviewWithUser>()
                var pending = reviewsSnapshot.size()

                reviewsSnapshot.documents.forEach { doc ->
                    val review = doc.toReview() ?: return@forEach
                    db.collection("users").document(review.user_id).get()
                        .addOnSuccessListener { userSnapshot ->
                            val user = userSnapshot.toObject(User::class.java)
                            val reviewWithUser = ReviewWithUser(
                                review = review,
                                userFirstName = user?.firstName,
                                userLastName = user?.lastName
                            )
                            tempList.add(reviewWithUser)

                            pending--
                            if (pending == 0) {
                                // Sort before posting to state
                                tempList.sortByDescending { it.review.created_at }
                                _reviews.addAll(tempList)
                            }
                        }
                }

                lastVisibleReview = reviewsSnapshot.documents.last()
            }
    }
}

private fun DocumentSnapshot.toReview(): Review? {
    return try {
        getDocumentReference("user_id")?.let { userDocRef ->
            getDocumentReference("cafe_id")?.let { cafeDocRef ->
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
                    description = getString("description") ?: "",
                    user_id = userDocRef.id,
                    cafe_id = cafeDocRef.id,
                    created_at = getTimestamp("created_at") ?: Timestamp.now(),
                )
            }
        }
    } catch (e: Exception) {
        null
    }
}
