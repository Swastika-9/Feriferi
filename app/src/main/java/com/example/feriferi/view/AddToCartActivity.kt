package com.example.feriferi.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.feriferi.ui.theme.FeriferiTheme
import com.example.feriferi.viewmodel.AddToCartViewModel

class AddToCartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeriferiTheme {

                val cartViewModel: AddToCartViewModel = viewModel()

                AddToCartScreen(
                    viewModel = cartViewModel,
                    onBackClick = { finish() },
                    onNavigateToDetails = { productId ->

                    }
                )
            }
        }
    }
}