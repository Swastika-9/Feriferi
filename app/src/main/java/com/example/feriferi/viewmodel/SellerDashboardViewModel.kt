package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.R
import com.example.feriferi.model.Product
import com.example.feriferi.model.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SellerDashboardViewModel : ViewModel() {

    private val _selectedTab = MutableStateFlow("Home")
    val selectedTab: StateFlow<String> = _selectedTab

    private val _seller = MutableStateFlow(
        Seller(
            name = "Vivienne Shirley",
            username = "@vivienne",
            profileImage = R.drawable.seller_profile,
            productsSold = 12
        )
    )
    val seller: StateFlow<Seller> = _seller

    private val _products = MutableStateFlow(
        listOf(
            Product("1", "Sandal", R.drawable.sandal),
            Product("2", "Floral Dress", R.drawable.floral_dress),
            Product("3", "Cotton Shirt", R.drawable.cotton_shirt)
        )
    )
    val products: StateFlow<List<Product>> = _products

    fun onTabChange(tab: String) {
        _selectedTab.value = tab
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filterNot { it.id == productId }
    }
}
