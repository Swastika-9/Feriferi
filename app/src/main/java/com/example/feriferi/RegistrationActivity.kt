package com.example.feriferi

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepoImpl
import com.example.feriferi.ui.theme.*
import com.example.feriferi.viewmodel.UserViewModel

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegisterScreen()
        }
    }
}

@Composable
fun RegisterScreen() {
    val userViewModel = remember { UserViewModel(UserRepoImpl()) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibility by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as Activity

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundColor)
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

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Full name", color = TextBrown) },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = TextBrown)
                },
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

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email", color = TextBrown) },
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = null, tint = TextBrown)
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

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = TextBrown) },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = TextBrown)
                },
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        Icon(
                            painter = if (visibility)
                                painterResource(R.drawable.baseline_visibility_off_24)
                            else painterResource(R.drawable.baseline_visibility_24),
                            contentDescription = null,
                            tint = TextBrown
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Role Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { selectedRole = "Buyer" },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sign up as buyer", color = WhiteText)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { selectedRole = "Seller" },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Sign up as seller", color = WhiteText)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("OR", color = TextBrown, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(15.dp))

            // Google Sign Up
            Button(
                onClick = { /* Google login */ },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painterResource(R.drawable.google), contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Sign up with Google", color = WhiteText)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Login link
            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = TextBrown)) { append("Log in") }
                },
                modifier = Modifier.clickable { activity.finish() }
            )
        }
    }
}

@Preview
@Composable
fun RegisterPreview() {
    RegisterScreen()
}