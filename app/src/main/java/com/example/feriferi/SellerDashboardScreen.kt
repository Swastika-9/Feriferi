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
import com.example.feriferi.model.Product
import com.example.feriferi.model.Seller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboard() {

    var screenIndex by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    // Combine your products with more from your friend
    var products by remember {
        mutableStateOf(
            listOf(
                Product("1", "Sandal", R.drawable.sandal),
                Product("2", "Floral Dress", R.drawable.floral_dress),
                Product("3", "Cotton Shirt", R.drawable.cotton_shirt),
                Product("4", "Shoes", R.drawable.shoes),
                Product("5", "Bag", R.drawable.bag),
                Product("6", "Watch", R.drawable.watch),
                Product("7", "Hat", R.drawable.hat)
            ).toMutableList()
        )
    }

    var seller by remember { mutableStateOf(
        Seller("Vivienne Shirley", "@vivienne", R.drawable.seller_profile, productsSold = 12)
    )}

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("फेरीPheri", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { screenIndex = 2 }) {
                        Icon(painter = painterResource(R.drawable.baseline_notifications_24), contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = screenIndex == 0,
                    onClick = { screenIndex = 0 },
                    icon = { Icon(painterResource(R.drawable.baseline_home_24), contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = screenIndex == 1,
                    onClick = { screenIndex = 1 },
                    icon = { Icon(painterResource(R.drawable.baseline_message_24), contentDescription = null) },
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
            when(screenIndex) {
                0 -> SellerDashboardHome(
                    seller = seller,
                    products = products,
                    onEditProfile = { screenIndex = 3 },
                    onAddProduct = { /* add product logic */ },
                    onEditProduct = { productId ->
                        Toast.makeText(context, "Edit $productId", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteProduct = { productId ->
                        products = products.filter { it.id != productId }.toMutableList()
                        // Firebase delete logic can be added here
                    }
                )
                1 -> PlaceholderScreen("Messages Screen") { screenIndex = 0 }
                2 -> PlaceholderScreen("Notifications Screen") { screenIndex = 0 }
                3 -> PlaceholderScreen("Edit Profile Screen") { screenIndex = 0 }
            }
        }
    }
}

@Composable
fun SellerDashboardHome(
    seller: Seller,
    products: List<Product>,
    onEditProfile: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (String) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(seller.name, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Text(seller.username, color = Color.Gray)
        Text("Products sold: ${seller.productsSold}")

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(seller.profileImage),
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

            OutlinedButton(onClick = onEditProfile, border = BorderStroke(1.dp, Color.Black)) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Edit profile")
            }

            OutlinedButton(onClick = onAddProduct, border = BorderStroke(1.dp, Color.Black)) {
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
                            painter = painterResource(product.image),
                            contentDescription = product.name,
                            modifier = Modifier
                                .size(120.dp)
                                .clickable { onEditProduct(product.id) }
                        )

                        Row(
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            IconButton(onClick = { onEditProduct(product.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                            IconButton(onClick = { onDeleteProduct(product.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Add, contentDescription = "Delete") // Replace with delete icon if you have
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(product.name, fontSize = 14.sp, textAlign = TextAlign.Center, maxLines = 2)
                }
            }
        }

        Spacer(Modifier.height(60.dp))
    }
}

@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onBack) { Text("Back") }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SellerDashboardPreview() {
    SellerDashboard()
}