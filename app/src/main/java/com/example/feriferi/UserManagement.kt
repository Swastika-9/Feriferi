package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class UserManagementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                UserManagementScreen()
            }
        }
    }
}

@Composable
fun UserManagementScreen() {
    val brownTheme = Color(0xFF8B6B61)
    val secondaryGrey = Color(0xFF757575)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Menu, contentDescription = null, tint = secondaryGrey)
            Text(
                text = "फेरिPheri",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = brownTheme
            )
            Icon(Icons.Default.Notifications, contentDescription = null, tint = secondaryGrey)
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFEEEEEE))

        Text(
            text = "Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = brownTheme,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 12.dp)
        )

        /* ---------- PROFILE IMAGE FROM DRAWABLE ---------- */
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(1.dp, Color.LightGray, CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.vivienne),
                contentDescription = "User Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        UserDetailItem("Full Name", "Vivenne Shirley")
        UserDetailItem("Username", "Vivenne")
        UserDetailItem("Phone Number", "970000000")

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SmallActionButton("Remove User", brownTheme.copy(alpha = 0.7f))
            SmallActionButton("Verify User", brownTheme.copy(alpha = 0.7f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Change Password",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PasswordInputBox("Current Password", "xxxxx", Modifier.weight(1f))
            PasswordInputBox("New Password", "xxxxxx", Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = brownTheme),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Done", color = Color.White, fontSize = 16.sp)
        }
    }
}

/* ---------- REUSABLE COMPOSABLES ---------- */

@Composable
fun UserDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = value, fontSize = 14.sp)
    }
}

@Composable
fun SmallActionButton(text: String, bgColor: Color) {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = bgColor),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.height(30.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 11.sp)
    }
}

@Composable
fun PasswordInputBox(label: String, hint: String, modifier: Modifier) {
    var text by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = hint, fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserManagementPreview() {
    UserManagementScreen()
}
