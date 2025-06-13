package com.izanhuang.cafe_hunter_android.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _user = mutableStateOf<FirebaseUser?>(auth.currentUser)
    val user: State<FirebaseUser?> = _user

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _user.value = auth.currentUser
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                _user.value = user
                if (user != null) {
                    createUserInFirestore(user)
                }
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun handleGoogleAuthResult(account: GoogleSignInAccount?, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                val user = auth.currentUser
                _user.value = user
                if (user != null) {
                    createUserInFirestore(user)
                }
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    private fun createUserInFirestore(user: FirebaseUser) {
        val userRef = db.collection("users").document(user.uid)
        val userData = mapOf(
            "uid" to user.uid,
            "email" to user.email,
            "displayName" to user.displayName.orEmpty(),
            "createdAt" to FieldValue.serverTimestamp()
        )

        userRef.set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("AuthViewModel", "User created/updated in Firestore.")
            }
            .addOnFailureListener {
                Log.e("AuthViewModel", "Failed to create user in Firestore", it)
            }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
    }
}
