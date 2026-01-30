@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.feriferi.view

import android.app.Activity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.feriferi.R
import com.example.feriferi.model.ProductModel
import com.example.feriferi.model.Seller
import com.example.feriferi.viewmodel.EditProductViewModel
import com.example.feriferi.viewmodel.SellerProfileViewModel

@Composable
fun SellerDashboardScreen(
    onNavigateToAddProduct: () -> Unit
) {
    val AdminBrown = Color(0xFF8D736B)
    val AdminBgWhite = Color(0xFFFFFFFF)
    val context = LocalContext.current
    val activity = context as? Activity

    var selectedTab by remember { mutableIntStateOf(0) }

    // State to toggle screens locally
    var showEditProfile by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductModel?>(null) }

    val sellerProfileViewModel: SellerProfileViewModel = viewModel()
    val editProductViewModel: EditProductViewModel = viewModel()

    // --- 1. COLLECT REAL DATA FROM VIEWMODEL ---
    val seller by sellerProfileViewModel.seller.collectAsState()
    val products by sellerProfileViewModel.products.collectAsState()

    // --- NAVIGATION LOGIC ---
    if (showEditProfile) {
        // UPDATED: Now calls your actual EditProfileScreen
        EditSellerProfileScreen(
            onBack = { showEditProfile = false }
        )
    } else if (editingProduct != null) {
        LaunchedEffect(editingProduct) {
            editingProduct?.let { p -> editProductViewModel.initializeProduct(p) }
        }
        PlaceholderScreen("Edit Product ${editingProduct?.name}", onBack = { editingProduct = null })
    } else {
        // --- DASHBOARD CONTENT ---
        Scaffold(
            containerColor = AdminBgWhite,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "फेरिPheri",
                            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AdminBrown)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* TODO: Open Side Drawer */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = AdminBrown)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Show Notifications */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = AdminBrown)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { /* Navigate to Chat */ },
                        icon = { Icon(Icons.Default.Chat, null) },
                        label = { Text("Chat") }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (selectedTab == 0) {
                    HomeContent(
                        seller = seller,
                        products = products,
                        adminBrown = AdminBrown,
                        onEditProfile = { showEditProfile = true }, // Triggers the screen switch
                        onAddProduct = onNavigateToAddProduct,
                        onEditProduct = { id -> editingProduct = products.find { it.id == id } }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    seller: Seller,
    products: List<ProductModel>,
    adminBrown: Color,
    onEditProfile: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(3) }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = seller.name,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF4A4A4A)
                    )
                )

                Text(
                    text = seller.username,
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray, fontFamily = FontFamily.Serif)
                )

                Text(
                    text = "Since 2021",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray, fontFamily = FontFamily.Serif)
                )
                Text(
                    text = "${seller.productsSold} products sold",
                    style = TextStyle(fontSize = 14.sp, color = Color.Gray, fontFamily = FontFamily.Serif)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- FIXED IMAGE LOGIC HERE ---
                val imageModel = if (seller.profileImageUrl is String && (seller.profileImageUrl as String).isEmpty()) {
                    R.drawable.seller_profile
                } else {
                    seller.profileImageUrl ?: R.drawable.seller_profile
                }

                AsyncImage(
                    model = imageModel,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onEditProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = adminBrown),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit profile")
                    }

                    Button(
                        onClick = onAddProduct,
                        colors = ButtonDefaults.buttonColors(containerColor = adminBrown),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add product")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Recently added",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4A4A4A)
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }
        }

        items(products) { product ->
            ProductGridItem(product, onEditProduct)
        }
    }
}

@Composable
fun ProductGridItem(product: ProductModel, onEdit: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        AsyncImage(
            model = product.imageUrls.firstOrNull() ?: R.drawable.placeholder_image,
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            style = TextStyle(fontSize = 12.sp, color = Color.Gray),
            maxLines = 1
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onEdit(product.id) }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier.size(12.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Edit",
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Go Back")
            }
        }
    }
}