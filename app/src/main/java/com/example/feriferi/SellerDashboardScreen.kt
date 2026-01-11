package com.example.feriferi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.ProductModel
import com.example.feriferi.model.Seller
import coil.compose.rememberAsyncImagePainter

sealed class SellerScreen {
    object Home : SellerScreen()
    object Messages : SellerScreen()
    object Settings : SellerScreen()
    object AddProduct : SellerScreen()
    object EditProduct : SellerScreen()
}

@Composable
fun SellerDashboardScreen(onNavigateToAddProduct: () -> Unit) {
    var currentScreen by remember { mutableStateOf<SellerScreen>(SellerScreen.Home) }

    val seller by remember { mutableStateOf(
        Seller("Vivienne Shirley", "@vivienne", R.drawable.seller_profile, productsSold = 12)
    ) }

    var products by remember { mutableStateOf(sampleProducts().toMutableList()) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen is SellerScreen.Home,
                    onClick = { currentScreen = SellerScreen.Home },
                    icon = { Icon(painterResource(R.drawable.baseline_add_home_24), contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = currentScreen is SellerScreen.Messages,
                    onClick = { currentScreen = SellerScreen.Messages },
                    icon = { Icon(painterResource(R.drawable.baseline_message_24), contentDescription = "Messages") },
                    label = { Text("Messages") }
                )
                NavigationBarItem(
                    selected = currentScreen is SellerScreen.Settings,
                    onClick = { currentScreen = SellerScreen.Settings },
                    icon = { Icon(painterResource(R.drawable.baseline_settings_24), contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                is SellerScreen.Home -> HomeContent(
                    seller = seller,
                    products = products,
                    onEditProfile = { /* TODO */ },
                    onAddProduct = onNavigateToAddProduct,
                    onEditProduct = { currentScreen = SellerScreen.EditProduct },
                    onDeleteProduct = { productId ->
                        products = products.filter { it.id != productId }.toMutableList()
                    }
                )
                is SellerScreen.Messages -> PlaceholderScreen("Messages") { currentScreen = SellerScreen.Home }
                is SellerScreen.Settings -> PlaceholderScreen("Settings") { currentScreen = SellerScreen.Home }
                is SellerScreen.AddProduct -> PlaceholderScreen("Add Product") { currentScreen = SellerScreen.Home }
                is SellerScreen.EditProduct -> PlaceholderScreen("Edit Product") { currentScreen = SellerScreen.Home }
            }
        }
    }
}

@Composable
fun HomeContent(
    seller: Seller,
    products: List<ProductModel>,
    onEditProfile: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (String) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = if (seller.profileImageUrl != null)
                rememberAsyncImagePainter(seller.profileImageUrl)
            else
                painterResource(seller.profileImage),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(seller.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(seller.username, color = Color.Gray)
        Text("Products sold: ${seller.productsSold}", color = Color.Gray)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onEditProfile,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Edit Profile")
            }
            Button(
                onClick = onAddProduct,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Add Product")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recently Added",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            products.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            product = product,
                            onEdit = { onEditProduct(product.id) },
                            onDelete = { onDeleteProduct(product.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProductCard(
    product: ProductModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // FIX: Use imageUrls list. If empty, show placeholder
            val imagePainter = if (product.imageUrls.isNotEmpty()) {
                rememberAsyncImagePainter(product.imageUrls[0])
            } else {
                painterResource(R.drawable.shoes)
            }

            Image(
                painter = imagePainter,
                contentDescription = product.name,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = "Rs. ${product.price}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            // Added Status Display (Available / Sold Out)
            Text(
                text = product.status,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (product.quantity > 0) Color(0xFF4CAF50) else Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Edit",
                    color = Color(0xFF6A493A),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onEdit() }
                )
                Text(
                    text = "Delete",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDelete() }
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) { Text("Go Home") }
        }
    }
}

// FIX: Updated sample data to match new ProductModel constructor
fun sampleProducts() = listOf(
    ProductModel(id = "1", name = "Sandal", price = 1200.0, imageUrls = emptyList(), quantity = 5),
    ProductModel(id = "2", name = "Floral Dress", price = 2500.0, imageUrls = emptyList(), quantity = 2),
    ProductModel(id = "3", name = "Cotton Shirt", price = 2200.0, imageUrls = emptyList(), quantity = 0), // Sold Out
    ProductModel(id = "4", name = "Shoes", price = 3000.0, imageUrls = emptyList(), quantity = 10),
    ProductModel(id = "5", name = "Bag", price = 1500.0, imageUrls = emptyList(), quantity = 1),
    ProductModel(id = "6", name = "Watch", price = 5000.0, imageUrls = emptyList(), quantity = 4),
    ProductModel(id = "7", name = "Hat", price = 800.0, imageUrls = emptyList(), quantity = 8)
)
