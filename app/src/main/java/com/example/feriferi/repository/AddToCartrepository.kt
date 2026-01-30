package com.example.feriferi.repository

import com.example.feriferi.model.AddToCartModel
import kotlinx.coroutines.flow.Flow

interface AddToCartRepository {
    fun addItemToCart(item: AddToCartModel, onComplete: (Boolean) -> Unit)

    fun getCartItems(): Flow<List<AddToCartModel>>

    fun updateQuantity(itemId: String, newQty: Int)

    fun deleteItem(itemId: String)

    fun isValidOffer(originalPrice: Double, offeredPrice: Double): Boolean
}