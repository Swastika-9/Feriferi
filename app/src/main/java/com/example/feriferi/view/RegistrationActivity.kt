package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.feriferi.R
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepoImpl
import com.example.feriferi.ui.theme.*
import com.example.feriferi.viewmodel.UserViewModel

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeriferiTheme {
                RegisterScreen()
            }
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
    var username by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
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

            // FULL NAME
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Full name", color = TextBrown) },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = TextBrown)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // EMAIL
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it.lowercase() },
                placeholder = { Text("Username", color = TextBrown) },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = TextBrown)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = TextBrown) },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = TextBrown)
                },
                visualTransformation = if (visibility)
                    VisualTransformation.None else PasswordVisualTransformation(),
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = TextFieldColor,
                    unfocusedContainerColor = TextFieldColor,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ROLE SELECTION
            Text(
                text = "Choose your role",
                color = TextBrown,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TextFieldColor, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedRole = "Buyer" }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRole == "Buyer",
                        onClick = { selectedRole = "Buyer" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ButtonColor,
                            unselectedColor = TextBrown
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign up as Buyer", color = TextBrown)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedRole = "Seller" }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRole == "Seller",
                        onClick = { selectedRole = "Seller" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ButtonColor,
                            unselectedColor = TextBrown
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign up as Seller", color = TextBrown)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // REGISTER BUTTON
            Button(
                onClick = {
                    if (fullName.isBlank() || username.isBlank() || email.isBlank() || password.isBlank() || selectedRole.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Create user with username included
                    val user = UserModel(
                        fullName = fullName,
                        email = email,
                        role = selectedRole,
                        username = username  // ← USERNAME INCLUDED
                    )

                    userViewModel.registerUser(
                        email = email,
                        user = user,
                        password = password,
                        onSuccess = {
                            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
                            activity?.startActivity(Intent(activity, LoginActivity::class.java))
                            activity?.finish()
                        },
                        onFailure = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Create Account", color = WhiteText)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // LOGIN LINK
            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = TextBrown, fontWeight = FontWeight.Bold)) {
                        append("Log in")
                    }
                },
                modifier = Modifier.clickable {
                    activity?.startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    FeriferiTheme {
        RegisterScreen()
    }
}