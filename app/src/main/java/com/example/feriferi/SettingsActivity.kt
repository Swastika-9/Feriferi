//package com.example.feriferi
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.feriferi.ui.theme.FeriferiTheme
//import com.example.feriferi.viewmodel.SettingsViewModel
//
//class SettingsActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            FeriferiTheme {
//                val settingsViewModel: SettingsViewModel= viewmodel
//                SettingsScreen(settingsViewModel)
//            }
//        }
//    }
//}
//
//@Composable
//fun SettingsScreen(viewModel: SettingsViewModel) {
//
//    val user by viewModel.user.collectAsState()
//
//    var fullName by remember { mutableStateOf("") }
//    var username by remember { mutableStateOf("") }
//    var phone by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//
//    LaunchedEffect(user) {
//        user?.let {
//            fullName = it.fullName
//            username = it.username
//            phone = it.phoneNumber
//        }
//    }
//
//    Scaffold { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .padding(16.dp)
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState()),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Image(
//                painter = painterResource(id = R.drawable.profile),
//                contentDescription = "Profile Image",
//                modifier = Modifier.size(110.dp)
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Button(onClick = { /* image picker later */ }) {
//                Text("Choose file")
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            OutlinedTextField(
//                value = fullName,
//                onValueChange = { fullName = it },
//                label = { Text("Full Name") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = username,
//                onValueChange = { username = it },
//                label = { Text("Username") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            OutlinedTextField(
//                value = phone,
//                onValueChange = { phone = it },
//                label = { Text("Phone Number") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Change Password",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.align(Alignment.Start)
//            )
//
//            OutlinedTextField(
//                value = newPassword,
//                onValueChange = { newPassword = it },
//                label = { Text("New Password") },
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    viewModel.saveProfile(
//                        fullName = fullName,
//                        username = username,
//                        phone = phone,
//                        newPassword = newPassword
//                    )
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Save Changes")
//            }
//        }
//    }
//}
