package com.example.feriferi.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.feriferi.viewmodel.AdminDashboardViewModel

class AdminDashboardActivity : ComponentActivity() {


    private val viewModel: AdminDashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val users by viewModel.users.observeAsState(initial = emptyList())
                    val products by viewModel.products.observeAsState(initial = emptyList())
                    val isLoading by viewModel.isLoading.observeAsState(initial = false)

                    AdminDashboardScreen(
                        users = users,
                        products = products,
                        isLoading = isLoading,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
