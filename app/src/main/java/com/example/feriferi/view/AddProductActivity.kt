package com.example.feriferi.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState // You need this import!
import androidx.lifecycle.viewmodel.compose.viewModel // You need this import!
import com.example.feriferi.ui.theme.FeriferiTheme
import com.example.feriferi.viewmodel.AddProductViewModel

class AddProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FeriferiTheme {
                // 1. Initialize the ViewModel
                val addProductViewModel: AddProductViewModel = viewModel()

                // 2. Observe the state from ViewModel
                val isUploading by addProductViewModel.isUploading.observeAsState(initial = false)
                val statusMessage by addProductViewModel.statusMessage.observeAsState()

                // 3. Show Toast when status changes
                LaunchedEffect(statusMessage) {
                    statusMessage?.let {
                        Toast.makeText(this@AddProductActivity, it, Toast.LENGTH_SHORT).show()
                        if (it == "Product Added Successfully!") finish()
                    }
                }

                AddProductScreen(
                    onBack = { finish() },
                    isUploading = isUploading,
                    onUpload = { product, uris ->
                        // 4. Trigger the sequential Cloudinary upload
                        addProductViewModel.uploadProductWithImages(product, uris)
                    }
                )
            }
        }
    }
}