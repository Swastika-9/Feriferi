package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

class ItemDescription2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemDescription2Screen()
        }
    }
}

@Composable
fun ItemDescription2Screen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4EDE4))
    ) {

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 160.dp) // leave space for sticky bottom section
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
                    contentDescription = "Product Image",
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
                        imageVector = Icons.Default.Bookmark,
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
                    Text("Deco Chair", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("Rs 1575", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "Original price: 20000",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text("@hooman", fontSize = 12.sp, color = Color.Gray)
                Text("200 products sold", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Product Details", fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(6.dp))

                // ---- DETAILS ----
                DetailItem("Color", "Soft Blue")
                DetailItem("Condition", "4.5/5")
                DetailItem("Bill", "Available")

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    InfoChip("Category: Furniture", Color(0xFFFFC107))
                    Spacer(modifier = Modifier.width(8.dp))
                    InfoChip("Status: Available", Color(0xFFE91E63))
                }
            }
        }

        /* ---------------- STICKY BOTTOM SECTION ---------------- */
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFF4EDE4))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Offer your Price row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Offer Price */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D6D6D)),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp)
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
                    Text("13000", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Add to Cart button
            Button(
                onClick = { /* Add to Cart */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6E63)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Add to Cart",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

/* ---------------- INLINE DETAIL ITEM ---------------- */
@Composable
fun DetailItem(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontSize = 13.sp)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

/* ---------------- INFO CHIP ---------------- */
@Composable
fun InfoChip(text: String, color: Color) {
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
fun PreviewItemDescription2() {
    ItemDescription2Screen()
}