package com.example.feriferi


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Data model ---
data class Vehicle(
    val name: String,
    val image: Int
)

// --- Activity ---
class VehicleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VehicleScreen()
        }
    }
}

// --- Main VehicleScreen Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleScreen(previewMode: Boolean = false) {

    val categories = listOf(
        "Clothing",
        "Foot-Wear",
        "Accessories",
        "Furniture",
        "Vehicle",
        "Electronics",
        "Books&Stationary"
    )

    val vehicles = listOf(
        Vehicle("Bajaj Pulsar 125", R.drawable.bajaj),
        Vehicle("Steel Mountain Bike", R.drawable.steelmountainbike),
        Vehicle("Sencor Scooter", R.drawable.sencorscooter),
        Vehicle("Jaguar F-Pace", R.drawable.jaguar)
    )

    var selectedCategory by remember { mutableStateOf("Vehicle") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "फेरीFeri",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->

        Row(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // Left category column
            LazyColumn(
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFEFE4D4))
            ) {
                items(categories.size) { index ->
                    val item = categories[index]
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = item }
                            .padding(12.dp),
                        fontWeight = if (item == selectedCategory) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            // Right vehicle grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(vehicles) { vehicle ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            // Preview-safe image: gray box
                            if (previewMode) {
                                Box(
                                    modifier = Modifier
                                        .height(110.dp)
                                        .fillMaxWidth()
                                        .background(Color.Gray)
                                )
                            } else {
                                Image(
                                    painter = painterResource(vehicle.image),
                                    contentDescription = vehicle.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(110.dp)
                                        .fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = vehicle.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "@Brand",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun VehicleScreenPreview() {
    VehicleScreen(previewMode = true)
}
