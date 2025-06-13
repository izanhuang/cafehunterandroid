package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.izanhuang.cafe_hunter_android.core.domain.AuthViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.LoginForm

@Composable
fun AuthScreen(viewModel: AuthViewModel = AuthViewModel()) {
    val user by viewModel.user

    if (user == null) {
        LoginForm(viewModel)
    } else {
        ProfileScreen(user!!)
    }
}

@Composable
fun ProfileScreen(user: FirebaseUser) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        user.photoUrl?.let { AsyncImage(model = it, contentDescription = "Profile picture") }
        Text("${user.displayName ?: user.email}")
        Button(onClick = { /* edit profile logic */ }) { Text("Edit Profile") }
        Button(onClick = { FirebaseAuth.getInstance().signOut() }) { Text("Sign Out") }
    }
}