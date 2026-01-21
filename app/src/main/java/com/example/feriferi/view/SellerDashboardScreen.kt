package com.example.feriferi.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.ProductModel
import com.example.feriferi.model.Seller
import coil.compose.rememberAsyncImagePainter
import com.example.feriferi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(onNavigateToAddProduct: () -> Unit) {
    // --- BRAND COLORS ---
    val AdminBrown = Color(0xFF8D736B)
    val AdminBgWhite = Color(0xFFFFFFFF)
    val AdminGray = Color(0xFF757575)

    var selectedTab by remember { mutableStateOf(0) } // 0: Home, 1: Messages, 2: Settings

    val seller by remember { mutableStateOf(
        Seller("Vivienne Shirley", "@vivienne", R.drawable.seller_profile, productsSold = 12)
    ) }

    var products by remember { mutableStateOf(sampleProducts().toMutableList()) }

    Scaffold(
        containerColor = AdminBgWhite,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "फेरिPheri",
                        style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AdminBrown)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(Icons.Default.Menu, null, tint = AdminBrown)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.NotificationsNone, null, tint = AdminBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AdminBgWhite)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AdminBrown,
                        selectedTextColor = AdminBrown,
                        indicatorColor = AdminBrown.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Email, contentDescription = "Messages") },
                    label = { Text("Messages") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdminBrown, selectedTextColor = AdminBrown)
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = AdminBrown, selectedTextColor = AdminBrown)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> HomeContent(
                    seller = seller,
                    products = products,
                    adminBrown = AdminBrown,
                    adminGray = AdminGray,
                    onEditProfile = { /* TODO */ },
                    onAddProduct = onNavigateToAddProduct,
                    onEditProduct = { /* TODO */ },
                    onDeleteProduct = { productId ->
                        products = products.filter { it.id != productId }.toMutableList()
                    }
                )
                1 -> PlaceholderScreen("Messages", { selectedTab = 0 })
                2 -> PlaceholderScreen("Settings", { selectedTab = 0 })
            }
        }
    }
}

@Composable
fun HomeContent(
    seller: Seller,
    products: List<ProductModel>,
    adminBrown: Color,
    adminGray: Color,
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

        // Profile Section
        Box(contentAlignment = Alignment.BottomEnd) {
            Image(
                painter = if (seller.profileImageUrl != null)
                    rememberAsyncImagePainter(seller.profileImageUrl)
                else
                    painterResource(seller.profileImage),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, adminBrown, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(seller.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(seller.username, color = adminGray)
        Text("Products sold: ${seller.productsSold}", color = adminGray, fontSize = 14.sp)

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
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = adminBrown)
            ) {
                Text("Edit Profile", color = Color.White)
            }
            Button(
                onClick = onAddProduct,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = adminBrown)
            ) {
                Text("Add Product", color = Color.White)
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
                            accentColor = adminBrown,
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
    accentColor: Color,
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
                maxLines = 1,
                fontSize = 16.sp
            )

            // RESTORED: Detailed Subtitle
            Text(
                text = "${product.category} | ${product.brand ?: "Generic"}",
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Text(
                text = "Rs. ${product.price}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor
            )

            // RESTORED: Times Worn Tag
            if (!product.tag.isNullOrEmpty()) {
                Surface(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "Worn: ${product.tag}",
                        fontSize = 10.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

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
            Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D736B))) {
                Text("Go Home")
            }
        }
    }
}

fun sampleProducts() = listOf(
    ProductModel(id = "1", name = "Sandal", price = 1200.0, category = "Footwear", brand = "Local", tag = "2 times", quantity = 5),
    ProductModel(id = "2", name = "Floral Dress", price = 2500.0, category = "Clothing", brand = "Zara", tag = "New", quantity = 2),
    ProductModel(id = "3", name = "Cotton Shirt", price = 2200.0, category = "Clothing", brand = "H&M", tag = "5 times", quantity = 0),
    ProductModel(id = "4", name = "Sports Shoes", price = 3000.0, category = "Footwear", brand = "Nike", tag = "10 times", quantity = 10),
    ProductModel(id = "5", name = "Luxury Bag", price = 1500.0, category = "Accessories", brand = "Gucci", tag = "New", quantity = 1)
)
