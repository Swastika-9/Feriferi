package com.example.feriferi.repository

import com.example.feriferi.R
import com.example.feriferi.model.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SellerRepository {
    private val _sellerData = MutableStateFlow(
        Seller("Vivienne Shirley", "@vivienne", R.drawable.seller_profile, productsSold = 12)
    )
    val sellerData: StateFlow<Seller> = _sellerData

    fun updateProfile(updatedSeller: Seller) {
        _sellerData.value = updatedSeller
    }
}