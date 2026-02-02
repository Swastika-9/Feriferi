package com.example.feriferi.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cloudinary.android.MediaManager
import com.example.feriferi.ui.theme.FeriferiTheme

// --- ADD THIS IMPORT ---

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkCloudinaryConnection()

        setContent {
            FeriferiTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onStartClick = {
                            // This now works because we imported LoginActivity
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                    )
                }
            }
        }
    }

    private fun checkCloudinaryConnection() {
        try {
            val cloudName = MediaManager.get().cloudinary.config.cloudName
            Toast.makeText(this, "SUCCESS: Connected to Cloud: $cloudName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR: Cloudinary NOT Initialized! Check Manifest.", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onStartClick: () -> Unit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onStartClick) {
            Text(text = "Go to Login")
        }
    }
}