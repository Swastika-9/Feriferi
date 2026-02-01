package com.example.feriferi.repository

import android.util.Log
import com.example.feriferi.model.LikedProducts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FavoriteRepository {

    private val TAG = "FavoriteRepository"

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    private fun userId() = auth.currentUser?.uid ?: ""

    /**
     * Toggles favorite status for a product
     * Structure: Users/{userId}/favorites/{productId}
     */
    suspend fun toggleFavorite(product: LikedProducts, isFavorite: Boolean) {
        if (userId().isEmpty()) {
            Log.e(TAG, "User not logged in")
            return
        }

        val ref = db.getReference("Users")
            .child(userId())
            .child("favorites")
            .child(product.productId)

        try {
            if (isFavorite) {
                // Add to favorites
                ref.setValue(product).await()
                Log.d(TAG, "Added favorite: ${product.productId}")
            } else {
                // Remove from favorites
                ref.removeValue().await()
                Log.d(TAG, "Removed favorite: ${product.productId}")
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Toggle favorite failed: ${ex.message}", ex)
            throw ex
        }
    }

    /**
     * Gets all favorited products for current user
     * Returns a list of LikedProducts
     */
    suspend fun getFavorites(): List<LikedProducts> {
        if (userId().isEmpty()) {
            Log.e(TAG, "User not logged in")
            return emptyList()
        }

        return try {
            val snapshot = db.getReference("Users")
                .child(userId())
                .child("favorites")
                .get()
                .await()

            val favorites = mutableListOf<LikedProducts>()

            snapshot.children.forEach { child ->
                val liked = child.getValue(LikedProducts::class.java)
                if (liked != null) {
                    favorites.add(liked)
                }
            }

            Log.d(TAG, "Fetched ${favorites.size} favorites for user ${userId()}")
            favorites
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to fetch favorites: ${ex.message}", ex)
            emptyList()
        }
    }

    /**
     * Checks if a specific product is favorited
     */
    suspend fun isFavorite(productId: String): Boolean {
        if (userId().isEmpty()) return false

        return try {
            val snapshot = db.getReference("Users")
                .child(userId())
                .child("favorites")
                .child(productId)
                .get()
                .await()

            snapshot.exists()
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to check favorite status: ${ex.message}", ex)
            false
        }
    }
}