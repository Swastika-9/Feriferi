package com.example.feriferi.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.feriferi.repository.ChatFairyRepoImpl
import com.example.feriferi.ui.theme.FeriferiTheme
import com.example.feriferi.viewmodel.ChatFairyViewModel
import com.example.feriferi.viewmodel.ChatFairyViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class ChatFairyActivity : ComponentActivity() {

    private val repo = ChatFairyRepoImpl()
    private val factory = ChatFairyViewModelFactory(repo)

    private val viewModel: ChatFairyViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: "guest_user"

        viewModel.fetchMessages(userId)

        setContent {
            FeriferiTheme {
                ChatFairyScreen(
                    viewModel = viewModel,
                    userId = userId,
                    onBackClick = { finish() }
                )
            }
        }
    }
}