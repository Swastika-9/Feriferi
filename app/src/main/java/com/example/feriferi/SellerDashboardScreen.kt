package com.example.feriferi

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.Product
import com.example.feriferi.model.Seller

@Composable
fun SellerDashboardScreen() {
    var selectedTab by remember { mutableStateOf("Home") }
    var currentScreen by remember { mutableStateOf("Home") }

    var seller by remember { mutableStateOf(Seller("Vivienne Shirley", "@vivienne", R.drawable.seller_profile, productsSold = 12)) }
    var products by remember { mutableStateOf(sampleProducts().toMutableList()) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home"; currentScreen = "Home" },
                    icon = { Icon(painterResource(R.drawable.baseline_add_home_24), contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == "Messages",
                    onClick = { selectedTab = "Messages"; currentScreen = "Messages" },
                    icon = { Icon(painterResource(R.drawable.baseline_message_24), contentDescription = null) },
                    label = { Text("Messages") }
                )
                NavigationBarItem(
                    selected = selectedTab == "Settings",
                    onClick = { selectedTab = "Settings"; currentScreen = "Settings" },
                    icon = { Icon(painterResource(R.drawable.baseline_settings_24), contentDescription = null) },
                    label = { Text("Settings") }
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            when (currentScreen) {
                "Home" -> HomeContent(
                    seller = seller,
                    products = products,
                    onEditProfile = { currentScreen = "EditProfile" },
                    onAddProduct = { currentScreen = "AddProduct" },
                    onEditProduct = { productId -> currentScreen = "EditProduct" },
                    onDeleteProduct = { productId ->
                        products = products.filter { it.id != productId }.toMutableList()
                    }
                )

            }
        }
    }
}

@Composable
fun HomeContent(
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(seller.profileImage),
            contentDescription = null,
            modifier = Modifier.size(120.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(seller.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(seller.username, color = Color.Gray)
        Text("Products sold: ${seller.productsSold}", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onEditProfile, modifier = Modifier.weight(1f)) { Text("Edit Profile") }
            Button(onClick = onAddProduct, modifier = Modifier.weight(1f)) { Text("Add Product") }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Recently Added", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth().padding(start = 16.dp))
        Spacer(modifier = Modifier.height(12.dp))

        // Grid style: 2 products per row
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            products.chunked(2).forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(
                            product = product,
                            onEdit = { onEditProduct(product.id) },
                            onDelete = { onDeleteProduct(product.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(product.image),
            contentDescription = null,
            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(product.name, fontWeight = FontWeight.Medium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Edit", color = Color(0xFF6A493A), modifier = Modifier.clickable { onEdit() })
            Text("Delete", color = Color.Red, modifier = Modifier.clickable { onDelete() })
        }
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

fun sampleProducts() = listOf(
    Product("1", "Sandal", 1200.0,2000.0,"M", "Zara Flip Flop Sandal. Only One Time Wore for an hour","Red","Great, Like Brand New",1,"Zara", "Tag available","FootWear","Women", "Available",R.drawable.sandal),
    Product("2", "Floral Dress", 2500.0, R.drawable.floral_dress),
    Product("3", "Cotton Shirt", 2200.0 R.drawable.cotton_shirt),
    Product("4", "Shoes", R.drawable.shoes),
    Product("5", "Bag", R.drawable.bag),
    Product("6", "Watch", R.drawable.watch),
    Product("7", "Hat", R.drawable.hat)
)
