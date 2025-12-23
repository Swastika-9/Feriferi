package com.example.feriferi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.ui.graphics.vector.ImageVector



@Composable
fun ItemDescriptionScreen() {

    val sizes = listOf("XS", "S", "M", "L", "XL")
    var selectedSize by remember { mutableStateOf("M") }

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
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Notification",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .size(20.dp)
            )
        }

        /* ---------------- IMAGE ---------------- */
        Box {
            AsyncImage(
                model = "https://via.placeholder.com/600x800",
                contentDescription = "Product",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Like",
                    tint = Color(0xFFE53935),
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White, CircleShape)
                        .padding(5.dp)
                )

                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = "Save",
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.White, CircleShape)
                        .padding(5.dp)
                )

            }
        }

        /* ---------------- CONTENT ---------------- */
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
                    text = "Relaxed Cotton Shirt",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Rs 600",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Original price: 1450",
                fontSize = 12.sp,
                color = Color.Gray,
                textDecoration = TextDecoration.LineThrough
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "@vivienne",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = "214 products sold",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Size", fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                sizes.forEach {
                    SizeChip(
                        text = it,
                        selected = it == selectedSize
                    ) { selectedSize = it }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Product Details", fontWeight = FontWeight.Medium)

            DetailRow("Color", "Cream")
            DetailRow("Condition", "4.5/5")
            DetailRow("Number of times worn", "2")
            DetailRow("Company", "H&M")
            DetailRow("Tags", "Available")

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                Chip("Category: Clothing", Color(0xFFFFC107))
                Spacer(modifier = Modifier.width(8.dp))
                Chip("Status: Available", Color(0xFFE91E63))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        /* ---------------- OFFER PRICE ---------------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D6D6D)),
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
            ) {
                Text("Offer your Price", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .height(48.dp)
                    .width(80.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "585", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

/* ---------------- COMPONENTS ---------------- */

@Composable
fun SizeChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 13.sp)
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewItem() {
    ItemDescriptionScreen()
}
