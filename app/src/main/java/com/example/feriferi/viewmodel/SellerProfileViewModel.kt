package com.example.feriferi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.R
import com.example.feriferi.model.ProductModel
import com.example.feriferi.model.Seller
import com.example.feriferi.repository.ProductRepoImpl
import com.example.feriferi.repository.SellerRepoImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SellerProfileViewModel : ViewModel() {
    private val productRepo = ProductRepoImpl()
    private val sellerRepo = SellerRepoImpl()
    private val auth = FirebaseAuth.getInstance()

    // --- STATE ---
    private val _seller = MutableStateFlow(
        Seller(
            name = "",
            username = "",
            profileImageUrl = R.drawable.seller_profile
        )
    )
    val seller: StateFlow<Seller> = _seller.asStateFlow()

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products.asStateFlow()

    init {
        fetchSellerData()
        fetchMyProducts()
    }

    private fun fetchSellerData() {
        val uid = auth.currentUser?.uid ?: return

        sellerRepo.getSellerData(uid) { success, _, fetchedSeller ->
            if (success && fetchedSeller != null) {
                val currentProductCount = _products.value.size
                _seller.value = fetchedSeller.copy(productsSold = currentProductCount)
            }
        }
    }

    private fun fetchMyProducts() {
        val uid = auth.currentUser?.uid ?: return

        productRepo.getAllProduct { success, _, list ->
            if (success && list != null) {
                val myProducts = list.filter { it.sellerId == uid }
                _products.value = myProducts

                val currentSeller = _seller.value
                _seller.value = currentSeller.copy(productsSold = myProducts.size)
            }
        }
    }

    // --- UPDATED FUNCTION WITH USERNAME PARAMETER ---
    fun updateProfile(
        uid: String,
        name: String,
        username: String, // <--- THIS IS THE MISSING PARAMETER
        phone: String,
        newImageUri: Uri?,
        currentImageUrl: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var finalImageUrl = currentImageUrl

                if (newImageUri != null) {
                    val uploadedUrl = sellerRepo.uploadProfileImage(newImageUri)
                    if (uploadedUrl.isNotEmpty()) {
                        finalImageUrl = uploadedUrl
                    }
                }

                // Clean the username (remove @)
                val cleanUsername = username.replace("@", "").trim()

                // Prepare Updates
                val updates = mapOf(
                    "fullName" to name,
                    "username" to cleanUsername, // <--- Saving the new username
                    "phone" to phone,
                    "profileImageUrl" to finalImageUrl
                )

                // Update Database
                sellerRepo.updateSellerProfile(uid, updates) { success, message ->
                    if (success) {
                        val currentCount = _products.value.size
                        _seller.value = _seller.value.copy(
                            name = name,
                            username = "@$cleanUsername", // Update UI immediately
                            phone = phone,
                            profileImageUrl = finalImageUrl,
                            productsSold = currentCount
                        )
                    }
                    onResult(success, message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, e.message ?: "Unknown Error occurred")
            }
        }
    }
}