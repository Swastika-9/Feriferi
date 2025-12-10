package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SellerDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SellerDashboardScreen(
                onEditProfile = { /* TODO */ },
                onAddProduct = { /* TODO */ },
                onEditProduct = { /* TODO */ }
            )
        }
    }
}

@Composable
fun SellerDashboardScreen(
    onEditProfile: () -> Unit = {},
    onAddProduct: () -> Unit = {},
    onEditProduct: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Home") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBarItem(
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" },
                    icon = { Icon(painterResource(R.drawable.home), contentDescription = null, tint = if (selectedTab == "Home") Color(0xFF6A493A) else Color.Gray) },
                    label = { Text("Home", color = if (selectedTab == "Home") Color(0xFF6A493A) else Color.Gray) }
                )

                NavigationBarItem(
                    selected = selectedTab == "Messages",
                    onClick = { selectedTab = "Messages" },
                    icon = { Icon(painterResource(R.drawable.message), contentDescription = null, tint = if (selectedTab == "Messages") Color(0xFF6A493A) else Color.Gray) },
                    label = { Text("Messages", color = if (selectedTab == "Messages") Color(0xFF6A493A) else Color.Gray) }
                )

                NavigationBarItem(
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings" },
                    icon = { Icon(painterResource(R.drawable.settings), contentDescription = null, tint = if (selectedTab == "Settings") Color(0xFF6A493A) else Color.Gray) },
                    label = { Text("Settings", color = if (selectedTab == "Settings") Color(0xFF6A493A) else Color.Gray) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            when (selectedTab) {
                "Home" -> HomeContent(onEditProfile, onAddProduct, onEditProduct)
                "Messages" -> PlaceholderScreen("Messages Screen")
                "Settings" -> PlaceholderScreen("Settings Screen")
            }
        }
    }
}

@Composable
fun HomeContent(
    onEditProfile: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // App Title
        Text(
            "फेरिPheri",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6A493A))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Profile Section
        Text("Vivienne Shirley", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("@vivienne", color = Color.Gray)
        Text("Since 2021", color = Color.Gray, fontSize = 14.sp)
        Text("21 products sold", color = Color.Gray, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.seller_profile),
            contentDescription = null,
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Profile & Add Product Buttons
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            Button(
                onClick = onEditProfile,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Edit profile")
            }

            Spacer(modifier = Modifier.width(15.dp))

            Button(
                onClick = onAddProduct,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add product")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Recently Added Section
        Text(
            "Recently added",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProductCard("Sandal", R.drawable.sandal, onEditProduct)
            ProductCard("Floral Dress", R.drawable.floral_dress, onEditProduct)
            ProductCard("Cotton Shirt", R.drawable.cotton_shirt, onEditProduct)
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProductCard(name: String, img: Int, onEdit: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(name, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))
        Text(
            "Edit",
            modifier = Modifier.padding(top = 5.dp),
            color = Color(0xFF6A493A)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSellerDashboard() {
    SellerDashboardScreen()
}
