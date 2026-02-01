//package com.example.feriferi.repository
//
//import com.example.feriferi.R
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//class ProductRepository {
//
//    private val _product = MutableStateFlow(
//        Product(
//            name = "Vivienne Dress",
//            color = "Cream",
//            condition = "4.5/5",
//            timesWorn = 2,
//            company = "H&M",
//            tag = "Available",
//            imageRes = R.drawable.vivienne
//        )
//    )
//
//    val product: StateFlow<Product> = _product
//
//    fun markAsSold() {
//        _product.value = _product.value.copy(
//            isSold = true,
//            tag = "Sold"
//        )
//    }
//
//    fun removeProduct() {
//        _product.value = _product.value.copy(
//            tag = "Removed"
//        )
//    }
//}
