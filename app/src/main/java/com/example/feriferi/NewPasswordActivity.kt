package com.example.feriferi

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.app.Activity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

class NewPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                NewPasswordScreen { password, confirmPassword ->
                    submitNewPassword(this, password, confirmPassword)
                }
            }
        }
    }
}

@Composable
fun NewPasswordScreen(onSubmitClick: (String, String) -> Unit) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFEF3E2),
                        Color(0xFFFFFFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Logo
            Text(
                text = "फेरिPheri",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF78350F)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Heading
            Text(
                text = "New Password",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // New Password Input Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter New Password", color = Color(0xFF6B7280)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color(0xFF78350F)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD97706),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFFEF3E2),
                    unfocusedContainerColor = Color(0xFFFEF3E2),
                    focusedTextColor = Color(0xFF1F2937),
                    unfocusedTextColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Input Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm Password", color = Color(0xFF6B7280)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Confirm Password",
                        tint = Color(0xFF78350F)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD97706),
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFFEF3E2),
                    unfocusedContainerColor = Color(0xFFFEF3E2),
                    focusedTextColor = Color(0xFF1F2937),
                    unfocusedTextColor = Color(0xFF1F2937)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = {
                    onSubmitClick(password, confirmPassword)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF78350F),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Send",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back to Login
            TextButton(
                onClick = {
                    activity?.startActivity(
                        android.content.Intent(activity, LoginActivity::class.java)
                    )
                    activity?.finish()
                }
            ) {
                Text(
                    text = "← Back to Log in",
                    color = Color(0xFF78350F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun submitNewPassword(context: Context, password: String, confirmPassword: String) {
    if (password != confirmPassword) {
        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
        return
    }

    val auth = FirebaseAuth.getInstance()
    auth.currentUser?.updatePassword(password)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    task.exception?.message ?: "Failed to update password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

@Preview(showBackground = true)
@Composable
fun NewPasswordPreview() {
    MaterialTheme {
        NewPasswordScreen { _, _ -> /* no-op for preview */ }
    }
}