package com.example.feriferi.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme

class  SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                // This works because we imported it above.
                // We don't pass 'showTopBar' because it defaults to true (which is what we want for this standalone screen)
                SettingsScreen()
            }
        }
    }
}