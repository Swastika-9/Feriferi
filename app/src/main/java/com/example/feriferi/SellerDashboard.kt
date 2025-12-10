package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SellerDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SellerDashboardScreen(
                onEditProfile = {
                    // TODO: navigate to Edit Profile Activity
                    // startActivity(Intent(this, EditProfileActivity::class.java))
                },
                onAddProduct = {
                    // TODO: navigate to Add Product Activity
                },
                onEditProduct = {
                    // TODO: navigate to Edit Product Page
                }
            )
        }
    }
}

@Composable
fun SellerDashboardScreen(
    onEditProfile: () -> Unit = {},
    onAddProduct: () -> Unit = {},
    onEditProduct: () -> Unit = {}
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // App Bar Mock
            Text(
                "फेरिPheri",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A493A)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Profile Section
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Vivienne Shirley", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("@vivienne", color = Color.Gray)
                Text("Since 2021", color = Color.Gray, fontSize = 14.sp)
                Text("21 products sold", color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(20.dp))

                // TODO: Replace `R.drawable.profile_placeholder` with your real seller profile image
                Image(
                    painter = painterResource(id = R.drawable.seller_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Button(
                        onClick = onEditProfile,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Edit profile")
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    Button(
                        onClick = onAddProduct,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add product")
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Recently Added
            Text(
                "Recently added",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // TODO: Replace product placeholders with your product images in drawable
                ProductCard("Sandal", R.drawable.sandal, onEditProduct)
                ProductCard("Floral Dress", R.drawable.floral_dress, onEditProduct)
                ProductCard("Cotton Shirt", R.drawable.cotton_shirt, onEditProduct)
            }

            Spacer(modifier = Modifier.height(70.dp))

            // Bottom Nav Mock
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // TODO: Replace with real icons (e.g., R.drawable.ic_home)
                    Image(painter = painterResource(id = R.drawable.home), contentDescription = null, modifier = Modifier.size(24.dp))
                    Text("Home")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.message), contentDescription = null, modifier = Modifier.size(24.dp))
                    Text("Messages")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.settings), contentDescription = null, modifier = Modifier.size(24.dp))
                    Text("Settings")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Home")
                Text("Messages")
                Text("Settings")
            }
        }
    }
}

@Composable
fun ProductCard(name: String, img: Int, onEdit: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = img),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(name, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))
        Text(
            "Edit",
            modifier = Modifier.padding(top = 5.dp),
            color = Color(0xFF6A493A)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSellerDashboard() {
    SellerDashboardScreen()
}
