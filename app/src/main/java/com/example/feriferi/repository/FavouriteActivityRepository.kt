package com.example.feriferi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FavoriteRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getFavorites(): List<FavoriteItem> {
        return try {
            val snapshot = db.collection("favorites").get().await()
            snapshot.toObjects(FavoriteItem::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}


