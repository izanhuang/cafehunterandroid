package com.izanhuang.cafe_hunter_android.core.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.izanhuang.cafe_hunter_android.R
import com.izanhuang.cafe_hunter_android.core.data.AppLogger
import com.izanhuang.cafe_hunter_android.core.domain.AuthViewModel

@Composable
fun LoginForm(authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val isFormValid = email.isNotBlank() && password.isNotBlank()

    val launcher = rememberGoogleSignInLauncher { authResult ->
        if (authResult != null) {
            authViewModel.handleGoogleAuthResult(
                authResult = authResult,
                onResult = { isSignedIn, message ->
                    if (isSignedIn) {
                        AppLogger.d("Google Sign In", "Signed in successfully")
                    } else {
                        AppLogger.e("Google Sign In", "Sign in failed: $message")
                        Toast.makeText(context, message ?: "Sign in failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Login", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("Email") },
            isError = emailError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        if (emailError != null) {
            Text(emailError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        if (passwordError != null) {
            Text(passwordError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Button(
            onClick = {
                var hasError = false

                if (email.isBlank()) {
                    emailError = "Email is required"
                    hasError = true
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailError = "Enter a valid email"
                    hasError = true
                }

                if (password.isBlank()) {
                    passwordError = "Password is required"
                    hasError = true
                }

                if (!hasError) {
                    authViewModel.login(email, password) { success, error ->
                        if (!success) {
                            Toast.makeText(context, error ?: "Login failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Login")
        }

        Divider(Modifier.padding(vertical = 12.dp))

        Button(
            onClick = {
                // ⬇️ Trigger Google Sign-In Launcher here
                val googleSignInClient = GoogleSignIn.getClient(
                    context,
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                )
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Google")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continue with Google")
        }
    }
}
