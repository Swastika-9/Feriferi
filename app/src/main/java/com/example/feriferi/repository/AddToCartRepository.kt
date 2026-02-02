package com.example.feriferi.repository

import com.example.feriferi.model.AddToCartModel
import com.example.feriferi.model.Item // Ensure this matches the model used in ItemDescription

interface AddToCartRepository {
    fun addToCart(product: Item, onResult: (Boolean) -> Unit)
    fun getCartItems(onUpdate: (List<AddToCartModel>) -> Unit)
    fun updateQuantity(cartId: String, newQty: Int)
    fun removeFromCart(cartId: String)
    fun clearCart()
}