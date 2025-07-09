package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.izanhuang.cafe_hunter_android.core.data.User
import com.izanhuang.cafe_hunter_android.core.domain.AuthViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.auth.EditNameDialog
import com.izanhuang.cafe_hunter_android.core.utils.Resource

@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val userDetails by authViewModel.userDetails.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.fetchUserProfile()
    }

    when (val state = userDetails) {
        is Resource.Success -> ProfileScreenDetails(state.data, authViewModel)
        is Resource.Error -> {}
        Resource.Loading -> LoadingScreen()
    }
}


@Composable
fun ProfileScreenDetails(userDetails: User, authViewModel: AuthViewModel) {
    var firstName by remember { mutableStateOf( userDetails.firstName) }
    var lastName by remember { mutableStateOf( userDetails.lastName) }

    var showFirstNameDialog by remember { mutableStateOf(false) }
    var showLastNameDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
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
        }

        Spacer(Modifier.height(16.dp))

        // First Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("First Name: ${firstName.ifBlank { "Not Set" }}")
            Spacer(Modifier.width(8.dp))
            Button(onClick = { showFirstNameDialog = true }) { Text("Edit") }
        }

        // Last Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Last Name: ${lastName.ifBlank { "Not Set" }}")
            Spacer(Modifier.width(8.dp))
            Button(onClick = { showLastNameDialog = true }) { Text("Edit") }
        }

        if (firstName.isBlank() || lastName.isBlank()) {
            Text(
                "If your first and last name are not set, your reviews will be anonymous.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // First Name Dialog
        if (showFirstNameDialog) {
            EditNameDialog(
                title = "Edit First Name",
                initialValue = firstName,
                onConfirm = {
                    firstName = it
                    authViewModel.updateFirstName(firstName)
                    showFirstNameDialog = false
                },
                onDismiss = { showFirstNameDialog = false }
            )
        }

        // Last Name Dialog
        if (showLastNameDialog) {
            EditNameDialog(
                title = "Edit Last Name",
                initialValue = lastName,
                onConfirm = {
                    lastName = it
                    authViewModel.updateLastName(lastName)
                    showLastNameDialog = false
                },
                onDismiss = { showLastNameDialog = false }
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