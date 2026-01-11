package com.example.feriferi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.feriferi.model.Item
import com.google.firebase.database.*

class ItemDescriptionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemId = intent.getStringExtra("itemId")
        if (itemId == null) {
            finish()
            return
        }

        val ref = FirebaseDatabase.getInstance()
            .getReference("items")
            .child(itemId)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Item::class.java)

                if (item != null) {
                    setContent {
                        ItemDescriptionScreen(item = item)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
