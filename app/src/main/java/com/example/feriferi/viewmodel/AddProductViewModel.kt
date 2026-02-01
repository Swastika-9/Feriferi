package com.example.feriferi.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.ProductModel
import com.example.feriferi.repository.ProductRepoImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AddProductViewModel : ViewModel() {

    private val repo = ProductRepoImpl()
    private val auth = FirebaseAuth.getInstance()

    // --- 1. LIVE DATA FOR UI STATE (Fixes "Unresolved reference") ---
    private val _isUploading = MutableLiveData(false)
    val isUploading: LiveData<Boolean> = _isUploading

    private val _statusMessage = MutableLiveData<String?>()
    val statusMessage: LiveData<String?> = _statusMessage

    // --- 2. UPLOAD FUNCTION (Fixes "Unresolved reference") ---
    fun uploadProductWithImages(product: ProductModel, imageUris: List<Uri>) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            _statusMessage.value = "User not logged in!"
            return
        }

        _isUploading.value = true // Show loading spinner

        viewModelScope.launch {
            try {
                // Prepare the product with the correct Seller ID
                val newProduct = product.copy(
                    sellerId = uid,
                    imageUrls = emptyList() // The repo will fill this after upload
                )

                // Take the first image if available (Since our Repo supports single upload currently)
                val primaryImageUri = imageUris.firstOrNull()

                // Call the Repo
                val result = repo.addProduct(newProduct, primaryImageUri)

                if (result.first) {
                    _statusMessage.value = "Product Added Successfully!"
                } else {
                    _statusMessage.value = "Error: ${result.second}"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error: ${e.message}"
            } finally {
                _isUploading.value = false // Hide loading spinner
            }
        }
    }

    // Helper to reset message after showing Toast
    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}