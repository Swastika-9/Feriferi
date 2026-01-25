package com.example.feriferi.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.feriferi.model.Item
import com.google.firebase.database.*

class ItemDescriptionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemId = intent.getStringExtra("itemId")
        if (itemId == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            var item by remember { mutableStateOf<Item?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(itemId) {
                val ref = FirebaseDatabase.getInstance()
                    .getReference("items")
                    .child(itemId)

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        item = snapshot.getValue(Item::class.java)
                        isLoading = false

                        if (item == null) {
                            Toast.makeText(
                                this@ItemDescriptionActivity,
                                "Item not found in database",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        isLoading = false
                        Toast.makeText(
                            this@ItemDescriptionActivity,
                            "Failed to load item",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                })
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                item?.let {
                    ItemDescriptionScreen(item = it)
                }
            }
        }
    }
}
