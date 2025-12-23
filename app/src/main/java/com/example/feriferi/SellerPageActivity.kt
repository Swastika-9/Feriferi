package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class SellerPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellerPageScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerPageScreen() {
    Scaffold(
        topBar = { SellerTopBar() },
        bottomBar = { SellerBottomBar() },
        containerColor = Color(0xFFF6EEE8)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Search bar with icon
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile image
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Seller Profile",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                tint = Color(0xFF8D6E63)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Vivienne Shirley", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("@vivienne", fontSize = 14.sp, color = Color.Gray)
            Text("Since 2021 · 21 products sold", fontSize = 12.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8D6E63)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Follow")
                }

                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Message")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recently added",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                ProductIcon("Beach Sandals")
                ProductIcon("Halter Floral Sundress")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8D6E63)
                )
            ) {
                Text("Add to Cart", fontSize = 16.sp)
            }
        }
    }
}

/* ---------------- TOP BAR ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "फेरीPheri",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B4E45)
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* back/home */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            TextButton(onClick = { /* report user */ }) {
                Text(
                    text = "Report user as scam",
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }
        }
    )
}

/* ---------------- BOTTOM BAR ---------------- */

@Composable
fun SellerBottomBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Home")
            },
            label = { Text("Home") }
        )
    }
}

/* ---------------- PRODUCT ---------------- */

@Composable
fun ProductIcon(title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = title,
            modifier = Modifier.size(90.dp),
            tint = Color(0xFF8D6E63)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SellerPagePreview() {
    SellerPageScreen()
}
