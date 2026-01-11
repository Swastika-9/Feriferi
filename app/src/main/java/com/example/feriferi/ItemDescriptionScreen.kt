package com.example.feriferi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feriferi.model.Item
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun ItemDescriptionScreen(item: Item) {

    var offerPrice by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4EDE4))
    ) {

        /* ---------------- TOP BAR ---------------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .size(20.dp)
            )

            Text(
                text = "फेरीPheri",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(20.dp)
            )
        }

        /* ---------------- PRODUCT IMAGE ---------------- */
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
                tint = Color(0xFFE53935),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(28.dp)
                    .background(Color.White, CircleShape)
                    .padding(5.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

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

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "@${item.sellerName}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "${item.soldCount} products sold",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Size", fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item.sizes.forEach {
                    SizeChip(
                        text = it,
                        selected = false, // always show selected
                        onClick = { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Product Details", fontWeight = FontWeight.Medium)
            DetailRow("Color:", item.color)
            DetailRow("Condition:", item.condition)
            DetailRow("Purchased Year:", item.purchasedYear.toString())
            DetailRow("Company:", item.brand)

            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { Chip("Category: ${item.category}", Color(0xFFFFC107))
                Chip("Status: ${item.status}", Color(0xFFE91E63)) } }
        Spacer(modifier = Modifier.weight(1f))

        Column(modifier = Modifier.padding(12.dp)) {

            // Offer your price
            OutlinedTextField(
                value = offerPrice,
                onValueChange = { offerPrice = it },
                label = { Text("Enter your offer") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val maxDiscount = item.price * 0.10
                    val minPrice = item.price - maxDiscount
                    val userOffer = offerPrice.text.toDoubleOrNull()

                    if (userOffer == null) {
                        Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                    } else if (userOffer < minPrice) {
                        Toast.makeText(
                            context,
                            "You can reduce the price only till Rs ${minPrice.toInt()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Offer submitted!", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = offerPrice.text.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D6D6D)),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Offer your Price")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Add to cart logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6E63)),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Add to Cart",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SizeChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .border(
                1.dp,
                if (selected) Color.Black else Color.Gray,
                RoundedCornerShape(6.dp)
            )
            .background(
                if (selected) Color.Black else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Black,
            fontSize = 13.sp
        )
    }
}

@Composable
fun DetailRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(title, fontSize = 13.sp)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun Chip(text: String, color: Color) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 12.sp,
        modifier = Modifier
            .background(color, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemDescription() {
    ItemDescriptionScreen(
        item = Item(
            name = "Relaxed Cotton Shirt",
            price = 600,
            originalPrice = 1450,
            sellerName = "vivienne",
            soldCount = 214,
            sizes = listOf("XS","S","M","L","XL"),
            color = "Cream",
            condition = "4.5/5",
            purchasedYear = 2022,
            brand = "H&M",
            category = "Clothing",
            status = "Available"
        )
    )
}