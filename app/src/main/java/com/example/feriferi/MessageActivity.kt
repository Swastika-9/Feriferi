package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette
val BrandBrown = Color(0xFF5D4037)
val SearchBarBeige = Color(0xFFF3E5D8)
val MessageBubblePink = Color(0xFFD9B7B7)
val ChipSelectedRed = Color(0xFF9E5C5C)

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MessageScreen()
            }
        }
    }
}

data class Chat(
    val name: String,
    val image: Int
)

@Composable
fun MessageScreen() {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, null) }, selected = false, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Search, null) }, selected = false, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Add, null) }, selected = false, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Mail, null) }, selected = true, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Person, null) }, selected = false, onClick = {})
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBackIosNew, null, modifier = Modifier.size(20.dp))
                Text("फेरिPheri", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandBrown)
                Icon(Icons.Default.Notifications, null)
            }

            // Search Bar
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                color = SearchBarBeige,
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Search, null, tint = Color.Gray)
                    Text(" Search", color = Color.Gray)
                }
            }

            Text(
                "Chats",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp)
            )

            // Filter Buttons
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = ChipSelectedRed)
                ) {
                    Text("All")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
                ) {
                    Text("Unread", color = Color.Black)
                }
            }

            // Chat List with Images
            val chats = listOf(
                Chat("Carloine", R.drawable.caroline),
                Chat("Situ Nakarmi", R.drawable.situ),
                Chat("Albert", R.drawable.albert),
                Chat("Rida", R.drawable.ridha)
            )

            LazyColumn {
                items(chats) { chat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // Profile Image
                        Image(
                            painter = painterResource(id = chat.image),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(chat.name, fontWeight = FontWeight.Bold)

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MessageBubblePink)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    "Thank you for your purchase ma’am. Keep shopping with us <3",
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMessageActivity() {
    MaterialTheme {
        MessageScreen()
    }
}
