package com.example.feriferi.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.feriferi.ui.theme.FeriferiTheme

class SellerDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeriferiTheme {
                SellerDashboardScreen(
                    onNavigateToAddProduct = {
                        val intent = Intent(this, AddProductActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}