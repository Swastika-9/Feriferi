package com.example.feriferi.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.feriferi.model.Item
import com.example.feriferi.repository.AddToCartRepoImpl
import com.example.feriferi.repository.ChatRepository
import com.example.feriferi.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ItemDescriptionScreen(item: Item) {

    val context = LocalContext.current
    var offerPrice by remember { mutableStateOf("") }

    // Dependencies
    val chatRepo = remember { ChatRepository() }
    val cartRepo = remember { AddToCartRepoImpl() }

    val currentUser = FirebaseAuth.getInstance().currentUser

    // Helper logic to get the correct image URL
    val displayImage = remember(item) {
        if (item.imageUrls.isNotEmpty()) item.imageUrls[0] else item.imageUrl
    }

    // Navigation Helper
    fun navigateToDashboard(tabIndex: Int) {
        val intent = Intent(context, DashboardActivity::class.java).apply {
            putExtra("SELECTED_TAB", tabIndex)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        context.startActivity(intent)
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navigateToDashboard(0) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFF4EDE4))
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navigateToDashboard(1) },
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Messages") },
                    label = { Text("Messages") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFF4EDE4))
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navigateToDashboard(2) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color(0xFFF4EDE4))
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4EDE4))
                .padding(innerPadding)
        ) {

            // --- 1. TOP BAR ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { (context as? Activity)?.finish() },
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    Text(
                        text = "फेरीPheri",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            val intent = Intent(context, NotificationActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                }
            }

            // --- 2. IMAGE SECTION ---
            item {
                Box {
                    if (displayImage.isNotEmpty()) {
                        AsyncImage(
                            model = displayImage,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(420.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.Red,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .padding(6.dp)
                    )
                }
            }

            // --- 3. DETAILS SECTION ---
            item {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Rs ${item.price}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Original price: Rs ${item.originalPrice}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (item.description.isNotEmpty()) {
                        Text(
                            text = "Description",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.description,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text("@${item.sellerName}", fontSize = 12.sp, color = Color.Gray)
                    Text("${item.soldCount} products sold", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Chat Button
                    Button(
                        onClick = {
                            if (currentUser == null) {
                                Toast.makeText(context, "Please login to chat", Toast.LENGTH_SHORT).show()
                            } else if (item.sellerId.isEmpty()) {
                                Toast.makeText(context, "Seller info unavailable", Toast.LENGTH_SHORT).show()
                            } else if (currentUser.uid == item.sellerId) {
                                Toast.makeText(context, "You cannot chat with yourself!", Toast.LENGTH_SHORT).show()
                            } else {
                                val chatId = chatRepo.getChatId(currentUser.uid, item.sellerId)
                                val intent = Intent(context, ChatActivity::class.java).apply {
                                    putExtra("chatId", chatId)
                                    putExtra("otherUserId", item.sellerId)
                                    putExtra("otherUserName", item.sellerName)
                                }
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chat with Seller")
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Size", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.sizes.forEach { SizeChip(text = it) }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Product Details", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRowVertical("Color", item.color)
                    DetailRowVertical("Condition", item.condition)
                    DetailRowVertical("Purchased Year", if (item.purchasedYear == 0) "N/A" else item.purchasedYear.toString())
                    DetailRowVertical("Company", item.brand)
                    DetailRowVertical("Tag", item.status)

                    Spacer(modifier = Modifier.height(20.dp))

                    // Offer Input
                    OutlinedTextField(
                        value = offerPrice,
                        onValueChange = { offerPrice = it },
                        label = { Text("Enter your offer") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- UPDATED OFFER BUTTON LOGIC ---
                    Button(
                        onClick = {
                            val userOffer = offerPrice.toIntOrNull()
                            val minPrice = (item.price * 0.9).toInt() // Example rule: min 90% of price

                            when {
                                userOffer == null -> Toast.makeText(context, "Enter a valid price", Toast.LENGTH_SHORT).show()
                                userOffer < minPrice -> Toast.makeText(context, "You can reduce price only till Rs $minPrice", Toast.LENGTH_SHORT).show()
                                else -> {
                                    Toast.makeText(context, "Offer sent to seller", Toast.LENGTH_SHORT).show()

                                    val buyerName = if (currentUser?.displayName.isNullOrEmpty()) "A Buyer" else currentUser!!.displayName
                                    val buyerId = currentUser?.uid ?: ""

                                    // NEW: SEND ACTIONABLE NOTIFICATION WITH ALL DETAILS
                                    NotificationRepository.sendNotification(
                                        targetUserId = item.sellerId,
                                        title = "New Offer!",
                                        message = "$buyerName offered Rs $userOffer for '${item.name}'",
                                        type = "offer",
                                        // Pass extra fields for Accept/Reject Buttons
                                        senderId = buyerId,
                                        productId = item.id,
                                        productImage = displayImage,
                                        offerPrice = userOffer.toDouble()
                                    )
                                }
                            }
                        },
                        enabled = offerPrice.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D6D6D))
                    ) {
                        Text("Offer your Price")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- ADD TO CART FUNCTIONALITY ---
                    Button(
                        onClick = {
                            cartRepo.addToCart(item) { success ->
                                if (success) {
                                    Toast.makeText(context, "Added to Cart!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, AddToCartActivity::class.java)
                                    context.startActivity(intent)
                                } else {
                                    Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6E63))
                    ) {
                        Text("Add to Cart", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun SizeChip(text: String) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.Black, RoundedCornerShape(6.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(text, fontSize = 13.sp)
    }
}

@Composable
fun DetailRowVertical(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(text = "$label:", fontSize = 13.sp, color = Color.Gray, modifier = Modifier.width(120.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}