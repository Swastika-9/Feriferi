package com.example.feriferi.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun SellerMessageScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("No messages yet", fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SellerMessagePreview() {
    SellerMessageScreen()
}
