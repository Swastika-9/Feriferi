package com.example.feriferi.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.ProductModel
import com.example.feriferi.model.Seller
import com.example.feriferi.repository.ProductRepoImpl
import com.example.feriferi.repository.SellerRepoImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SellerDashboardViewModel : ViewModel() {
    private val productRepo = ProductRepoImpl()
    private val sellerRepo = SellerRepoImpl()
    private val auth = FirebaseAuth.getInstance()

    // --- STATE ---
    private val _seller = MutableStateFlow(
        Seller(
            id = "",
            name = "Loading...",
            username = "",
            profileImageUrl = "",
            productsSold = 0,
            joiningDate = ""
        )
    )
    val seller: StateFlow<Seller> = _seller.asStateFlow()

    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products.asStateFlow()

    init {
        fetchDashboardData()
    }

    // --- 1. REFRESH FUNCTION (Fixes Image Update) ---
    // We call this when we come back from the Edit Screen
    fun refreshDashboard() {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        val uid = auth.currentUser?.uid ?: return

        // Fetch Profile
        sellerRepo.getSellerData(uid) { success, _, fetchedSeller ->
            if (success && fetchedSeller != null) {
                // We do NOT overwrite productsSold here anymore.
                // It will now strictly use what is in the "Users" database.
                _seller.value = fetchedSeller
            }
        }

        // Fetch Products
        productRepo.getAllProduct { success, _, list ->
            if (success && list != null) {
                val myProducts = list.filter { it.sellerId == uid }
                _products.value = myProducts

            }
        }
    }

    fun updateProfile(
        uid: String,
        name: String,
        username: String,
        phone: String,
        newImageUri: Uri?,
        currentImageUrl: String,
        onResult: (Boolean, String) -> Unit
    ) {
    }
}