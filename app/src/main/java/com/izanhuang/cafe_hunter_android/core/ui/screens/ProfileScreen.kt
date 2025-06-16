package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.izanhuang.cafe_hunter_android.core.domain.AuthViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.EditNameDialog

@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val user = authViewModel.user.collectAsState()
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val showFirstNameDialog = remember { mutableStateOf(false) }
    val showLastNameDialog = remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user.value?.uid?.let { uid ->
            authViewModel.fetchUserProfile(uid) { first, last ->
                firstName.value = first ?: ""
                lastName.value = last ?: ""
            }
        }
    }

    Column(
        Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
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
        }

        Spacer(Modifier.height(16.dp))

        // First Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("First Name: ${firstName.value.ifBlank { "Not Set" }}")
            Spacer(Modifier.width(8.dp))
            Button(onClick = { showFirstNameDialog.value = true }) { Text("Edit") }
        }

        // Last Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Last Name: ${lastName.value.ifBlank { "Not Set" }}")
            Spacer(Modifier.width(8.dp))
            Button(onClick = { showLastNameDialog.value = true }) { Text("Edit") }
        }

        if (firstName.value.isBlank() || lastName.value.isBlank()) {
            Text(
                "If your first and last name are not set, your reviews will be anonymous.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // First Name Dialog
        if (showFirstNameDialog.value) {
            EditNameDialog(
                title = "Edit First Name",
                initialValue = firstName.value,
                onConfirm = {
                    firstName.value = it
                    authViewModel.updateUserField("firstName", it)
                    showFirstNameDialog.value = false
                },
                onDismiss = { showFirstNameDialog.value = false }
            )
        }

        // Last Name Dialog
        if (showLastNameDialog.value) {
            EditNameDialog(
                title = "Edit Last Name",
                initialValue = lastName.value,
                onConfirm = {
                    lastName.value = it
                    authViewModel.updateUserField("lastName", it)
                    showLastNameDialog.value = false
                },
                onDismiss = { showLastNameDialog.value = false }
            )
        }

        Button(
            onClick = { authViewModel.logout() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.LightGray
            )
        ) {
            Text("Sign Out", color = Color.White)
        }
    }
}
