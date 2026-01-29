package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.R
import com.example.feriferi.model.ProductModel
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
            ProductModel(
                id = "1",
                name = "Sandal",
                price = 1200.0,
                imageUrls = emptyList(),
                quantity = 5
            ),
            ProductModel(
                id = "2",
                name = "Floral Dress",
                price = 2500.0,
                imageUrls = emptyList(),
                quantity = 2
            ),
            ProductModel(
                id = "3",
                name = "Cotton Shirt",
                price = 2200.0,
                imageUrls = emptyList(),
                quantity = 0
            )
        )
    )
    val products: StateFlow<List<ProductModel>> = _products

    fun onTabChange(tab: String) {
        _selectedTab.value = tab
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filterNot { it.id == productId }
    }
}