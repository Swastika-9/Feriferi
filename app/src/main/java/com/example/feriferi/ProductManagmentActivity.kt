package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ProductManagementActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                ProductManagementScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen() {
    val brownTheme = Color(0xFF8B6B61)
    val grayAction = Color(0xFF757575)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* ---------- HEADER ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = grayAction,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "फेरिPheri",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = brownTheme
            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = grayAction,
                modifier = Modifier.size(20.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp),
            color = Color.LightGray.copy(alpha = 0.4f)
        )

        Text(
            text = "Products Management",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(vertical = 8.dp)
        )

        /* ---------- PRODUCT IMAGE (ONE IMAGE ONLY) ---------- */
        Card(
            modifier = Modifier
                .size(160.dp)
                .border(0.5.dp, Color.LightGray),
            shape = RoundedCornerShape(2.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.vivienne),
                contentDescription = "Product Managment",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        /* ---------- PRODUCT DETAILS ---------- */
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Product Details",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            val detailSize = 12.sp
            ProductLine("Color", "Cream", detailSize)
            ProductLine("Condition", "4.5/5", detailSize)
            ProductLine("Number of times worn", "2", detailSize)
            ProductLine("Company", "H&M", detailSize)
            ProductLine("Tag", "Available", detailSize)
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------- ACTION BUTTONS ---------- */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = grayAction),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Mark as sold", fontSize = 10.sp)
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = grayAction),
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Remove Product", fontSize = 10.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* ---------- DONE BUTTON ---------- */
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = brownTheme),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = "Done",
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

/* ---------- REUSABLE LINE ---------- */
@Composable
fun ProductLine(
    label: String,
    value: String,
    size: androidx.compose.ui.unit.TextUnit
) {
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(text = "$label: ", fontWeight = FontWeight.Bold, fontSize = size)
        Text(text = value, fontSize = size)
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 580)
@Composable
fun ProductManagementPreview() {
    ProductManagementScreen()
}
