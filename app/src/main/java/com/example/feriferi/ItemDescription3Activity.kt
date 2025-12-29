package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.feriferi.R

class ItemDescription3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ItemDescription3Screen()
        }
    }
}

@Composable
fun ItemDescription3Screen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4EDE4))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 160.dp)
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
                Image(
                    painter = painterResource(id = R.drawable.greenshirt),
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
                    Text("Green linen shirt", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("Rs 1775", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "Original price: 1900",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text("@lennox", fontSize = 12.sp, color = Color.Gray)
                Text("200 products sold", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Product Details", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(6.dp))

                // -------- INLINE DETAILS --------
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Color", fontSize = 13.sp)
                    Text("Green", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Condition", fontSize = 13.sp)
                    Text("4.5/5", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Tag", fontSize = 13.sp)
                    Text("Available", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Number of times worn", fontSize = 13.sp)
                    Text("2", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Company", fontSize = 13.sp)
                    Text("zara", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(10.dp))

                // -------- INLINE CHIPS --------
                Row {
                    Text(
                        text = "Category: Clothes",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color(0xFFFFC107), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Status: Available",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color(0xFFE91E63), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }
        }

        /* ---------------- STICKY BOTTOM ---------------- */
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFF4EDE4))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { },
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

            Button(
                onClick = { },
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewItemDescription3() {
    ItemDescription3Screen()
}
