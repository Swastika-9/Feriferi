package com.example.feriferi.view

import androidx.compose.foundation.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.feriferi.R
import com.example.feriferi.model.AddToCartModel
import com.example.feriferi.viewmodel.AddToCartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCartScreen(
    viewModel: AddToCartViewModel,
    onBackClick: () -> Unit,
    onNavigateToDetails: (String) -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val pheriBrown = Color(0xFF8D736B)
    val lightGray = Color(0xFFD9D9D9)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("फेरिPheri", color = pheriBrown, fontWeight = FontWeight.Bold, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = pheriBrown)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF9F3F0))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            Text(
                "Shopping cart",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp).align(Alignment.CenterHorizontally)
            )

            // Gray Item Count Bar
            Surface(color = lightGray, modifier = Modifier.fillMaxWidth().height(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text("${cartItems.size} Items in your cart", fontWeight = FontWeight.Medium)
                }
            }

            if (cartItems.isEmpty()) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.increaseQuantity(item) },
                            onDecrease = { viewModel.decreaseQuantity(item) },
                            onItemClick = { onNavigateToDetails(item.id) }
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F3F0)),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        SummaryRow("Subtotal", "RS ${viewModel.getSubtotal().toInt()}")
                        SummaryRow("Shipping", "RS ${viewModel.shippingFee.toInt()}")
                        HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray)
                        SummaryRow("Total", "RS ${viewModel.getTotal().toInt()}", isBold = true)

                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = pheriBrown),
                            modifier = Modifier.fillMaxWidth().padding(top = 20.dp).height(54.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Check Out", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: AddToCartModel,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = if (item.imageUrl.isNullOrEmpty()) painterResource(R.drawable.cottonshirt)
            else rememberAsyncImagePainter(item.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .size(85.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(item.brand, color = Color.Gray, fontSize = 14.sp)

            // Highlight "Offer Accepted" status
            if (item.status == "Accepted") {
                Text("Offer Accepted", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            } else {
                Text("Waiting for Seller", color = Color(0xFFFFA000), fontSize = 12.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${item.quantity}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Column {
                Icon(
                    Icons.Default.Add, "Add",
                    Modifier.size(24.dp).clickable { onIncrease() }
                )
                Icon(
                    Icons.Default.Remove, "Remove",
                    Modifier.size(24.dp).clickable { onDecrease() }
                )
            }
        }

        Text(
            "RS ${item.price.toInt()}",
            fontWeight = FontWeight.Black,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        val style = if (isBold) FontWeight.Bold else FontWeight.Normal
        val size = if (isBold) 22.sp else 18.sp
        Text(label, fontSize = size, fontWeight = style)
        Text(value, fontSize = size, fontWeight = style)
    }
}