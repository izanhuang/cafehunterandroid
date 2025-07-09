package com.izanhuang.cafe_hunter_android.core.ui.components.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.izanhuang.cafe_hunter_android.core.data.AppLogger

@Composable
fun rememberGoogleSignInLauncher(onAuth: (AuthResult?) -> Unit) =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
        try {
            val acct = task.getResult(ApiException::class.java)
            val cred = GoogleAuthProvider.getCredential(acct.idToken, null)
            FirebaseAuth.getInstance()
                .signInWithCredential(cred)
                .addOnSuccessListener { result ->
                    onAuth(result) // valid AuthResult
                }
                .addOnFailureListener { e ->
                    AppLogger.e("GoogleSignIn", "Firebase auth failed", e)
                    onAuth(null) // signal failure
                }
        } catch (e: ApiException) {
            AppLogger.e("GoogleSignIn", "Google sign in failed", e)
            onAuth(null) // signal failure
        }
    }

