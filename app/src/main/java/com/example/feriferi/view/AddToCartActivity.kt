package com.example.feriferi.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feriferi.viewmodel.AddToCartViewModel

// Make sure your Theme import is correct, or remove it if not using a custom theme wrapper
// import com.example.feriferi.ui.theme.FeriferiTheme

class AddToCartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // If you have a custom theme, wrap this inside it like: FeriferiTheme { ... }

            val cartViewModel: AddToCartViewModel = viewModel()

            AddToCartScreen(
                viewModel = cartViewModel,
                onBackClick = {
                    finish()
                },
                onNavigateToDetails = { productId ->
                    // --- IMPLEMENTED NAVIGATION ---
                    val intent = Intent(this, ItemDescriptionActivity::class.java)
                    intent.putExtra("itemId", productId)
                    startActivity(intent)
                }
            )
        }
    }
}