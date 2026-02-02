package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel : ViewModel() {

    private val _allProducts = MutableStateFlow<List<ProductModel>>(emptyList())
    val allProducts: StateFlow<List<ProductModel>> = _allProducts.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchAllProducts()
    }

    private fun fetchAllProducts() {
        val ref = FirebaseDatabase.getInstance().getReference("Products")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductModel>()
                for (child in snapshot.children) {
                    val product = child.getValue(ProductModel::class.java)
                    if (product != null) {
                        product.id = child.key ?: "" // Ensure ID is saved
                        productList.add(product)
                    }
                }
                // Reverse to show newest first
                _allProducts.value = productList.reversed()
                _isLoading.value = false
            }

            override fun onCancelled(error: DatabaseError) {
                _isLoading.value = false
            }
        })
    }
}