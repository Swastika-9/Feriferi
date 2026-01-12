

package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ReviewRatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReviewRatingActivity()
        }
    }
}



/* -------------------- DATA MODEL -------------------- */

data class ReviewProduct(
    val brand: String,
    val name: String,
    val price: Int,
    val qty: Int,
    val image: Int
)


/* -------------------- SCREEN -------------------- */

@Composable
fun ReviewRatingsScreen() {

    val products = listOf(
        ReviewProduct("NewMew", "Yellow Shaded Glass", 1100, 1, R.drawable.yellowshadeglass),
        ReviewProduct("Miniso", "Transparent 1000ml water bottle", 580, 1, R.drawable.waterbottle)
    )

    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { BottomNav() },
        containerColor = Background
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Review and Ratings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn {
                items(products) {
                    BannerSection()
                    ReviewItem(it)
                    SectionTitle()

                }
            }
        }
    }
}



@Composable
fun ReviewItem(product: ReviewProduct) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Image(
                painter = painterResource(product.image),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(product.brand, fontWeight = FontWeight.Bold)
                Text(product.name)
                Text("Rs. ${product.price}")

                Text(
                    text = "Write a Review",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable {
                            // Handle review click
                        }
                )
            }

            Text("Qty:${product.qty}")
        }

        HorizontalDivider(color = Color.Black, thickness = 1.dp)
    }
}

/* -------------------- BOTTOM NAV -------------------- */

@Composable
fun BottomNavMain() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(CardBg),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painterResource(R.drawable.baseline_home_24), contentDescription = null)
        Icon(painterResource(R.drawable.baseline_shopping_cart_24), contentDescription = null)
        Icon(painterResource(R.drawable.baseline_person_24), contentDescription = null)
        Icon(painterResource(R.drawable.baseline_settings_24), contentDescription = null)
    }
}

/* ---------------- TOP BAR ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    TopAppBar(
        title = {
            Text("फेरिPheri", fontWeight = FontWeight.Bold)

        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu  ,
                contentDescription = null
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null
            )
        }
    )
}

/* ---------------- BANNER ---------------- */

@Composable
fun BannerSection() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFE6D3B1))
                .padding(16.dp)
                .height(130.dp)
        ) {
            Column {
                Text(
                    "Yours, Truly.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Timeless things, made yours again.\nShop better today.",
                    fontSize = 14.sp
                )
            }
        }
    }
}

/* ---------------- TITLE ---------------- */

@Composable
fun SectionTitle()  {
    Text(
        "Review and Ratings",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp)
    )
}



/* -------------------- THEME -------------------- */

private val Background = Color(0xFFF5EBDD)
private val CardBg = Color(0xFFF0E2D0)

@Composable
fun feriferiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography(
            titleLarge = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            bodyMedium = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp
            )
        ),
        content = content
    )
}

@Preview
@Composable
fun PreviewReviewRating() {
    ReviewRatingsScreen()
}