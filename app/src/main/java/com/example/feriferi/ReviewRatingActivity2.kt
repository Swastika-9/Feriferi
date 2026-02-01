
package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.feriferi.view.ReviewRatingsScreen

class ReviewRatingActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReviewRatingsScreen()
        }
    }
}

/* -------- MAIN SCREEN -------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewRatingsScreen2() {
    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomNav() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // -------- BANNER --------
            Image(
                painter = painterResource(R.drawable.banner), // replace with your image
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // -------- REVIEW LIST --------
            ReviewList()
        }
    }
}

/* -------- TOP APP BAR -------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "फेरिPheri",
                fontWeight = FontWeight.Bold
            )

        }
    )
}

/* -------- DATA MODEL -------- */
data class Review(
    val user: String,
    val product: String,
    val price: String,
    var rating: Int,
    val review: String
)

/* -------- REVIEW LIST -------- */
@Composable
fun ReviewList(modifier: Modifier = Modifier) {
    val reviews = remember {
        mutableStateListOf(
            Review(
                "NewMew",
                "Yellow Shaded Glass",
                "Rs. 1100",
                4,
                "The product is really good. Thank you PheriPheri"
            ),
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        items(reviews) { review ->
            ReviewCard(review)
        }
    }
}

/* -------- REVIEW CARD -------- */
@Composable
fun ReviewCard(review: Review) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = review.user,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Placeholder product image
            Image(
                painter = painterResource(R.drawable.yellowshadeglass),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.product,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = review.price,
                color = Color(0xFF3BB54A),
                fontWeight = FontWeight.Bold
            )

            StarRating(
                rating = review.rating,
                onRatingChange = { review.rating = it }
            )

            Text(
                text = review.review,
                modifier = Modifier.padding(top = 6.dp)
            )

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(Color(0xFF3BB54A)),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            ) {
                Text("Submit", color = Color.White)
            }
        }
    }
}

/* -------- STAR RATING -------- */
@Composable
fun StarRating(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row(modifier = Modifier.padding(top = 6.dp)) {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRatingChange(i) }
            )
        }
    }
}

/* -------- BOTTOM NAVIGATION -------- */
@Composable
fun BottomNav() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.ShoppingCart, null) },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Settings") }
        )
    }
}

/* -------- PREVIEW -------- */
@Preview
@Composable
fun PreviewReviewRating2() {
    ReviewRatingsScreen2()
}
