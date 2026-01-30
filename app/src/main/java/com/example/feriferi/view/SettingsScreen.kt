package com.example.feriferi.view.com.example.feriferi.view

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.feriferi.R
import com.example.feriferi.model.LikedProducts
import com.example.feriferi.model.ProductModel
import com.example.feriferi.repository.FavoriteRepository
import com.example.feriferi.repository.ProductRepoImpl
import com.example.feriferi.view.ItemDescriptionActivity
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val favoriteRepo = remember { FavoriteRepository() }
    val productRepo = remember { ProductRepoImpl() }

    // TEMP LOCAL STATE (no backend)
    var fullName by remember { mutableStateOf("Your Name") }
    var username by remember { mutableStateOf("username") }
    var phoneNumber by remember { mutableStateOf("98XXXXXXXX") }

    var likedProducts by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    /* ───────── FETCH LIKED POSTS ONLY ───────── */
    LaunchedEffect(Unit) {
        val likedList = favoriteRepo.getFavorites()
        val likedIds = likedList.map { it.productId }

        productRepo.getAllProduct { success, _, products ->
            if (success && products != null) {
                likedProducts = products.filter { it.id in likedIds }
            }
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF8F3))
            .padding(16.dp)
    ) {

        /* ───── SEARCH ───── */
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        /* ───── PROFILE ───── */
        Row {

            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                EditableProfileField("Full Name", fullName) { fullName = it }
                EditableProfileField("Username", username) { username = it }
                EditableProfileField("Phone Number", phoneNumber) { phoneNumber = it }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* saved later */ },
                    modifier = Modifier.width(220.dp)
                ) {
                    Text("Save Changes")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ───── LIKED POSTS ───── */
        Text("Liked Posts", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        if (likedProducts.isEmpty()) {
            Text("No liked posts yet", color = Color.Gray)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(likedProducts) { product ->
                    LikedProductItem(
                        product = product,
                        onUnlike = {
                            scope.launch {
                                favoriteRepo.toggleFavorite(
                                    LikedProducts(productId = product.id),
                                    isFavorite = false
                                )
                                likedProducts =
                                    likedProducts.filter { it.id != product.id }
                            }
                        },
                        onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    ItemDescriptionActivity::class.java
                                ).putExtra("productId", product.id)
                            )
                        }
                    )
                }
            }
        }
    }
}

/* ───────── COMPONENTS ───────── */

@Composable
fun EditableProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier.width(220.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun LikedProductItem(
    product: ProductModel,
    onUnlike: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            Column {
                AsyncImage(
                    model = product.imageUrls.firstOrNull(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                Column(modifier = Modifier.padding(8.dp)) {
                    Text(product.name, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Rs. ${product.price}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(
                onClick = onUnlike,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_favorite_24),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}