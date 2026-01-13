package com.example.feriferi

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.Item

@Composable
fun ItemDescriptionScreen(item: Item) {

    val context = LocalContext.current
    var offerPrice by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4EDE4))
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                )

                Text(
                    text = "फेरीPheri",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                )
            }
        }
        item {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.cottonshirt),
                    contentDescription = "Product Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp)
                )

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(28.dp)
                        .background(Color.White, CircleShape)
                        .padding(5.dp)
                )
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
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

                Spacer(modifier = Modifier.height(6.dp))

                Text("@${item.sellerName}", fontSize = 12.sp, color = Color.Gray)
                Text("${item.soldCount} products sold", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(14.dp))

                Text("Size", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.sizes.forEach {
                        SizeChip(text = it)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Product Details",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                DetailRowVertical("Color", item.color)
                DetailRowVertical("Condition", item.condition)
                DetailRowVertical(
                    "Purchased Year",
                    if (item.purchasedYear == 0) "N/A" else item.purchasedYear.toString()
                )
                DetailRowVertical("Company", item.brand)
                DetailRowVertical("Tag", item.status)

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = offerPrice,
                    onValueChange = { offerPrice = it },
                    label = { Text("Enter your offer") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        val userOffer = offerPrice.toIntOrNull()
                        val minPrice = (item.price * 0.9).toInt()

                        when {
                            userOffer == null ->
                                Toast.makeText(context, "Enter a valid price", Toast.LENGTH_SHORT).show()

                            userOffer < minPrice ->
                                Toast.makeText(
                                    context,
                                    "You can reduce the price only till Rs $minPrice",
                                    Toast.LENGTH_SHORT
                                ).show()

                            else ->
                                Toast.makeText(context, "Offer sent to seller", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = offerPrice.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D6D6D))
                ) {
                    Text("Offer your Price")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6E63))
                ) {
                    Text(
                        text = "Add to Cart",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemDescriptionScreen() {
    ItemDescriptionScreen(
        item = Item(
            name = "Relaxed Cotton Shirt",
            price = 600,
            originalPrice = 1450,
            sellerName = "vivienne",
            soldCount = 214,
            sizes = listOf("S", "M", "L"),
            color = "Cream",
            condition = "4.5/5",
            purchasedYear = 2022,
            brand = "H&M",
            category = "Clothing",
            status = "Available"
        )
    )
}