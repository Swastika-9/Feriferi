package com.example.feriferi.repository

import android.util.Log
import com.example.feriferi.model.LikedProducts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FavoriteRepository {

    private val TAG = "FavoriteRepository"

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private fun userId() = auth.currentUser?.uid ?: ""

    suspend fun toggleFavorite(product: LikedProducts, isFavorite: Boolean) {
        val ref = db.collection("users")
            .document(userId())
            .collection("likedPosts")
            .document(product.productId)

        try {
            if (isFavorite) {
                ref.set(product).await()
                Log.d(TAG, "Added favorite for product=${product.productId} user=${userId()}")
            } else {
                ref.delete().await()
                Log.d(TAG, "Removed favorite for product=${product.productId} user=${userId()}")
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Favorite toggle failed for product=${product.productId}: ${ex.message}", ex)
            throw ex
        }
    }

    suspend fun getFavorites(): List<LikedProducts> {
        return try {
            val list = db.collection("users")
                .document(userId())
                .collection("likedPosts")
                .get()
                .await()
                .toObjects(LikedProducts::class.java)
            Log.d(TAG, "Fetched favorites count=${list.size} for user=${userId()}")
            list
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to fetch favorites: ${ex.message}", ex)
            emptyList()
        }
    }
}
