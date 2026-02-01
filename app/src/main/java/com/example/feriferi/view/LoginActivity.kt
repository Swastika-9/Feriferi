package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.R
import com.example.feriferi.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeriferiTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val activity = context as? Activity

    val auth = if (!isPreview) FirebaseAuth.getInstance() else null
    val database = if (!isPreview) FirebaseDatabase.getInstance().getReference("Users") else null

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Surface(
        color = BackgroundColor,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "फेरिPheri",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = TextBrown,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = TextBrown) },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.baseline_email_24), contentDescription = null, tint = TextBrown)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = TextBrown) },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.baseline_lock_24), contentDescription = null, tint = TextBrown)
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = if (passwordVisible)
                                painterResource(R.drawable.baseline_visibility_24)
                            else
                                painterResource(R.drawable.baseline_visibility_off_24),
                            contentDescription = null,
                            tint = TextBrown
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "Forgot password ?",
                    color = Color(0xFF78350F),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        if (!isPreview) {
                            context.startActivity(Intent(context, ForgotPasswordActivity::class.java))
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                CircularProgressIndicator(color = ButtonColor)
            } else {
                Button(
                    onClick = {
                        if (isPreview) return@Button

                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true

                        auth?.signInWithEmailAndPassword(email, password)
                            ?.addOnSuccessListener {
                                val userId = auth.currentUser?.uid
                                if (userId == null) {
                                    isLoading = false
                                    Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
                                    return@addOnSuccessListener
                                }

                                Log.d("LoginActivity", "User logged in: $userId")

                                // Fetch the entire user node to debug
                                Log.d("LoginActivity", "Attempting to read from: users/$userId")

                                database
                                    ?.child(userId)
                                    ?.get()
                                    ?.addOnSuccessListener { snapshot ->
                                        isLoading = false

                                        Log.d("LoginActivity", "Snapshot exists: ${snapshot.exists()}")
                                        Log.d("LoginActivity", "User data: ${snapshot.value}")
                                        Log.d("LoginActivity", "Snapshot children count: ${snapshot.childrenCount}")

                                        // Check if user node exists
                                        if (!snapshot.exists()) {
                                            Toast.makeText(context, "User data not found in database. Please check Firebase.", Toast.LENGTH_LONG).show()
                                            Log.e("LoginActivity", "User node does not exist in database!")
                                            return@addOnSuccessListener
                                        }

                                        // Get role from snapshot
                                        val role = snapshot.child("role").getValue(String::class.java)

                                        Log.d("LoginActivity", "Role found: $role")

                                        // Normalize role to lowercase for comparison
                                        val normalizedRole = role?.lowercase()?.trim()

                                        when {
                                            normalizedRole == null || normalizedRole.isBlank() -> {
                                                Toast.makeText(context, "Login Error: Role missing in database", Toast.LENGTH_LONG).show()
                                            }
                                            normalizedRole == "admin" -> {
                                                Log.d("LoginActivity", "Opening Admin Dashboard")
                                                context.startActivity(Intent(context, AdminDashboardActivity::class.java))
                                                activity?.finish()
                                            }
                                            normalizedRole == "seller" -> {
                                                Log.d("LoginActivity", "Opening Seller Dashboard")
                                                context.startActivity(Intent(context, SellerDashboardActivity::class.java))
                                                activity?.finish()
                                            }
                                            normalizedRole == "buyer" -> {
                                                Log.d("LoginActivity", "Opening Buyer Dashboard")
                                                context.startActivity(Intent(context, DashboardActivity::class.java))
                                                activity?.finish()
                                            }
                                            else -> {
                                                Toast.makeText(context, "Unknown Role: $normalizedRole", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                    ?.addOnFailureListener { exception ->
                                        isLoading = false
                                        Log.e("LoginActivity", "Database read failed!", exception)
                                        Log.e("LoginActivity", "Exception type: ${exception.javaClass.simpleName}")
                                        Log.e("LoginActivity", "Exception message: ${exception.message}")
                                        Toast.makeText(context, "Database Error: ${exception.message}\nCheck if you're using Realtime Database (not Firestore)", Toast.LENGTH_LONG).show()
                                    }
                            }
                            ?.addOnFailureListener { e ->
                                isLoading = false
                                val errorMsg = when(e) {
                                    is com.google.firebase.auth.FirebaseAuthInvalidUserException -> "Account not found."
                                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Incorrect password."
                                    else -> e.message ?: "Login failed."
                                }
                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
                ) {
                    Text("Log in", color = WhiteText)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("OR", color = TextBrown, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Google Sign In not implemented yet", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(painter = painterResource(R.drawable.google), contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Unspecified)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log in with Google", color = WhiteText, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", color = TextBrown)
                Text(
                    "Sign Up",
                    color = ButtonColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        if (!isPreview) {
                            context.startActivity(Intent(context, RegistrationActivity::class.java))
                        }
                    }
                )
            }
        }
    }
}