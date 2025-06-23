package com.izanhuang.cafe_hunter_android.core.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izanhuang.cafe_hunter_android.LocalAuthViewModel
import com.izanhuang.cafe_hunter_android.core.ui.components.LoginForm
import com.izanhuang.cafe_hunter_android.core.ui.components.RegisterForm

@Composable
fun AuthScreen() {
    val authViewModel = LocalAuthViewModel.current
    val user by authViewModel.user.collectAsState()

    if (user == null) {
        val isRegistering = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isRegistering.value) {
                RegisterForm(authViewModel)
            } else {
                LoginForm(authViewModel)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { isRegistering.value = !isRegistering.value }) {
                Text(if (isRegistering.value) "Already have an account? Login" else "No account? Register")
            }
        }
    } else {
        ProfileScreen(authViewModel)
    }
}