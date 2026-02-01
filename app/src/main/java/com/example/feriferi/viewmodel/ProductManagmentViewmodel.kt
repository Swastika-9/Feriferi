package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Product(
    val color: String,
    val condition: String,
    val timesWorn: Int,
    val company: String,
    val tag: String,
    val imageRes: Int
)

class ProductViewModel : ViewModel() {

    private val _product = MutableStateFlow(
        Product(
            color = "Cream",
            condition = "4.5/5",
            timesWorn = 2,
            company = "H&M",
            tag = "Available",
            imageRes = R.drawable.vivienne
        )
    )

    val product: StateFlow<Product> = _product

    fun markAsSold() {
        _product.value = _product.value.copy(tag = "Sold")
    }

    fun removeProduct() {
        _product.value = _product.value.copy(tag = "Removed")
    }
}
