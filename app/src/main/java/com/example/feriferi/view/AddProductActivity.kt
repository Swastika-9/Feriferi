package com.example.feriferi.view

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.cloudinary.android.MediaManager
import com.example.feriferi.viewmodel.AddProductViewModel

class AddProductActivity : ComponentActivity() {

    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        try {
            MediaManager.get()
        } catch (e: Exception) {
            val config = mapOf(
                "cloud_name" to "dizcwwcat",
                "api_key" to "934843177742589",
                "api_secret" to "txri4GAHnxok5sBY0pB2gdiGMw4"
            )
            MediaManager.init(this, config)
        }

        setContent {
            val isUploading by viewModel.isUploading.observeAsState(initial = false)
            val statusMessage by viewModel.statusMessage.observeAsState(initial = "")

            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val cameraGranted = permissions[Manifest.permission.CAMERA] == true
                if (!cameraGranted) {
                    Toast.makeText(this, "Camera access is recommended for product photos", Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(Unit) {
                val permissionsNeeded = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                permissionLauncher.launch(permissionsNeeded)
            }

            LaunchedEffect(statusMessage) {
                if (statusMessage.isNotBlank()) {
                    Toast.makeText(this@AddProductActivity, statusMessage, Toast.LENGTH_SHORT).show()
                    if (statusMessage == "Product Added Successfully!") {
                        finish()
                    }
                }
            }

            AddProductScreen(
                onBack = { finish() },
                onUpload = { product, uris ->
                    viewModel.uploadProductWithImages(product, uris)
                },
                isUploading = isUploading
            )
        }
    }
}
