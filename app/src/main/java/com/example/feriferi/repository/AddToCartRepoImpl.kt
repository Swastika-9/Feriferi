package com.example.feriferi.repository
import com.example.feriferi.model.AddToCartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
class AddToCartRepoImpl: AddToCartRepository {

        private val auth = FirebaseAuth.getInstance()
        private val db = FirebaseDatabase.getInstance().getReference("carts")

        override fun addItemToCart(item: AddToCartModel, onComplete: (Boolean) -> Unit) {
            val userId = auth.currentUser?.uid ?: return
            // We use the product ID as the key to prevent duplicate entries
            db.child(userId).child(item.id).setValue(item)
                .addOnCompleteListener { onComplete(it.isSuccessful) }
        }

        override fun getCartItems(): Flow<List<AddToCartModel>> = callbackFlow {
            val userId = auth.currentUser?.uid ?: ""
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull {
                        it.getValue(AddToCartModel::class.java)
                    }
                    trySend(items) // Sends the list to the ViewModel
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            db.child(userId).addValueEventListener(listener)
            awaitClose { db.child(userId).removeEventListener(listener) }
        }

        override fun updateQuantity(itemId: String, newQty: Int) {
            val userId = auth.currentUser?.uid ?: return
            db.child(userId).child(itemId).child("quantity").setValue(newQty)
        }

        override fun deleteItem(itemId: String) {
            val userId = auth.currentUser?.uid ?: return
            db.child(userId).child(itemId).removeValue()
        }

        override fun isValidOffer(originalPrice: Double, offeredPrice: Double): Boolean {
            // Ensuring the offer is within the 10% limit
            val minPrice = originalPrice * 0.90
            return offeredPrice >= minPrice
        }
    }