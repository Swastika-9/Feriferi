//package com.example.feriferi.view
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.feriferi.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditProfileScreen(
//    onBack: () -> Unit
//) {
//
//    var fullName by remember { mutableStateOf("Vivienne Shirley") }
//    var username by remember { mutableStateOf("Vivienne") }
//    var phone by remember { mutableStateOf("970000000") }
//    var currentPassword by remember { mutableStateOf("") }
//    var newPassword by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onBack) {
//                Icon(
//                    painter = painterResource(R.drawable.baseline_arrow_back_24),
//                    contentDescription = "Back"
//                )
//            }
//            Text("Edit Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold)
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.Center
//        ) {
//            Image(
//                painter = painterResource(R.drawable.profilepic),
//                contentDescription = "Profile",
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape)
//            )
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        OutlinedTextField(
//            value = fullName,
//            onValueChange = { fullName = it },
//            label = { Text("Full Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        OutlinedTextField(
//            value = username,
//            onValueChange = { username = it },
//            label = { Text("Username") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(12.dp))
//
//        OutlinedTextField(
//            value = phone,
//            onValueChange = { phone = it },
//            label = { Text("Phone Number") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(16.dp))
//
//        Text("Change Password", fontWeight = FontWeight.SemiBold)
//
//        Spacer(Modifier.height(8.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            OutlinedTextField(
//                value = currentPassword,
//                onValueChange = { currentPassword = it },
//                label = { Text("Current") },
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.weight(1f)
//            )
//
//            OutlinedTextField(
//                value = newPassword,
//                onValueChange = { newPassword = it },
//                label = { Text("New") },
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.weight(1f)
//            )
//        }
//
//        Spacer(Modifier.height(24.dp))
//
//        Button(
//            onClick = onBack,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//        ) {
//            Text("Done")
//        }
//    }
//}