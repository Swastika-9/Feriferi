package com.example.feriferi.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.feriferi.model.Item
import com.google.firebase.database.*

class ItemDescriptionActivity : ComponentActivity() {

    private val TAG = "ItemDescriptionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Get the ID passed from Dashboard
        val itemId = intent.getStringExtra("itemId") ?: intent.getStringExtra("productId")

        if (itemId == null) {
            Toast.makeText(this, "Error: Item ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            var item by remember { mutableStateOf<Item?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var errorMessage by remember { mutableStateOf("") }

            LaunchedEffect(itemId) {
                // --- FIX: Capital "Products" to match your Database ---
                val ref = FirebaseDatabase.getInstance().getReference("Products").child(itemId)

                Log.d(TAG, "Fetching from: Products/$itemId")

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // Convert Firebase data to Item object
                            item = snapshot.getValue(Item::class.java)

                            // Ensure the ID is attached
                            if (item != null && item!!.id.isEmpty()) {
                                item!!.id = itemId
                            }
                            isLoading = false
                        } else {
                            Log.e(TAG, "No data found at Products/$itemId")
                            errorMessage = "Item not found. Check if ID matches."
                            isLoading = false
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        isLoading = false
                        errorMessage = "Database Error: ${error.message}"
                        Log.e(TAG, "DB Error: ${error.message}")
                    }
                })
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF5D4037))
                }
            } else if (errorMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = errorMessage)
                }
            } else {
                item?.let {
                    ItemDescriptionScreen(item = it)
                }
            }
        }
    }
}