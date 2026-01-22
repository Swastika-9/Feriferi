package com.example.feriferi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SellerDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SellerDashboard()
        }
    }
}

data class SellerProduct(
    val imageRes: Int,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboard() {

    var screenIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    val products = remember {
        mutableStateListOf(
            SellerProduct(R.drawable.sandal, "Handmade Sandal"),
            SellerProduct(R.drawable.dress, "Floral Dress"),
            SellerProduct(R.drawable.cottonshirt, "Cotton Shirt")
        )
    }

    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("फेरीPheri", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { screenIndex = 2 }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_notifications_24),
                            contentDescription = "Notifications"
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = screenIndex == 0,
                    onClick = { screenIndex = 0 },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_home_24),
                            contentDescription = null
                        )
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = screenIndex == 1,
                    onClick = { screenIndex = 1 },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_message_24),
                            contentDescription = null
                        )
                    },
                    label = { Text("Messages") }
                )
            }
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (screenIndex) {

                0 -> SellerDashboardHome(
                    products = products,
                    onEditProduct = { product ->
                        Toast.makeText(
                            context,
                            "Edit ${product.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onEditProfile = {
                        screenIndex = 3
                    }
                )

                1 -> SellerMessageScreen()

                2 -> SellerNotificationScreen()

                3 -> EditProfileScreen(
                    onBack = { screenIndex = 0 }
                )
            }
        }
    }
}

@Composable
fun SellerDashboardHome(
    products: List<SellerProduct>,
    onEditProduct: (SellerProduct) -> Unit,
    onEditProfile: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Vivienne Shirley", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Text("@vivienne", color = Color.Gray)
        Text("Since 2021")
        Text("21 products sold")

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.profilepic),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedButton(
                onClick = onEditProfile,
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Edit profile")
            }

            OutlinedButton(
                onClick = {},
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Add product")
            }
        }

        Spacer(Modifier.height(28.dp))

        Text("Recently added", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(products) { product ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(120.dp)
                ) {

                    Box {

                        Image(
                            painter = painterResource(product.imageRes),
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(120.dp)
                                .clickable { onEditProduct(product) }
                        )

                        IconButton(
                            onClick = { onEditProduct(product) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        product.name,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SellerDashboardPreview() {
    SellerDashboard()
}