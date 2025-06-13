package com.izanhuang.cafe_hunter_android.core.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return AuthViewModel() as T
            }
        }
    }

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

    fun handleGoogleAuthResult(
        authResult: AuthResult?,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = authResult?.user
        if (authResult == null || user == null) {
            onResult(false, "Google Sign-In failed. Try again.")
            return
        }

        _user.value = user
        createUserInFirestore(user)
        onResult(true, null)
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
