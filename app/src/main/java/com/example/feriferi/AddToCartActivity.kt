package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.feriferi.viewmodel.BuyerCartViewModel

class AddToCartActivity : ComponentActivity() {

    private val viewModel: BuyerCartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddToCartScreen(viewModel = viewModel)
        }
    }
}
