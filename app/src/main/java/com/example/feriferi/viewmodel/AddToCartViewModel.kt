package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.AddToCartModel
import com.example.feriferi.repository.AddToCartRepoImpl
import com.example.feriferi.repository.AddToCartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddToCartViewModel(
    private val repository: AddToCartRepository = AddToCartRepoImpl()
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<AddToCartModel>>(emptyList())
    val cartItems: StateFlow<List<AddToCartModel>> = _cartItems

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            // Collecting the Flow from the RepoImpl
            repository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    fun increaseQuantity(item: AddToCartModel) {
        repository.updateQuantity(item.id, item.quantity + 1)
    }

    fun decreaseQuantity(item: AddToCartModel) {
        if (item.quantity > 1) {
            repository.updateQuantity(item.id, item.quantity - 1)
        } else {
            repository.deleteItem(item.id)
        }
    }

    fun getSubtotal(): Double = _cartItems.value.sumOf { it.price * it.quantity }
    val shippingFee: Double = 70.0
    fun getTotal(): Double = getSubtotal() + shippingFee
}