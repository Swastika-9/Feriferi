package com.example.feriferi.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.feriferi.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ProductRepoImpl {

    private val db = FirebaseDatabase.getInstance()
    private val productsRef = db.getReference("Products")

    // --- READ PRODUCTS ---
    fun getAllProduct(callback: (Boolean, String?, List<ProductModel>?) -> Unit) {
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductModel>()
                for (child in snapshot.children) {
                    val product = child.getValue(ProductModel::class.java)
                    if (product != null) {
                        productList.add(product)
                    }
                }
                callback(true, "Success", productList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    // --- ADD PRODUCT ---
    suspend fun addProduct(
        product: ProductModel,
        imageUri: Uri?
    ): Pair<Boolean, String> {
        return try {
            var finalImageUrl = ""

            // Upload Image first
            if (imageUri != null) {
                finalImageUrl = uploadToCloudinary(imageUri)
            }

            val newProductId = productsRef.push().key ?: return Pair(false, "Database Error")

            val finalProduct = product.copy(
                id = newProductId,
                imageUrls = if (finalImageUrl.isNotEmpty()) listOf(finalImageUrl) else emptyList()
            )

            suspendCancellableCoroutine<Unit> { continuation ->
                productsRef.child(newProductId).setValue(finalProduct)
                    .addOnSuccessListener { continuation.resume(Unit) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }

            Pair(true, "Product Added Successfully")
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(false, e.message ?: "Failed to add product")
        }
    }

    // --- CLOUDINARY UPLOAD ---
    private suspend fun uploadToCloudinary(uri: Uri): String = suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .unsigned("product_images") // <--- THIS IS THE FIX. IT MATCHES YOUR SCREENSHOT.
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    continuation.resume(resultData["secure_url"].toString())
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(Exception("Cloudinary Error: ${error.description}"))
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {}
            })
            .dispatch()
    }
}