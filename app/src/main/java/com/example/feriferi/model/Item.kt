package com.example.feriferi.model

import com.google.firebase.database.PropertyName

data class Item(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Int = 0,
    var originalPrice: Int = 0,
    var sellerName: String = "",
    var sellerId: String = "",
    var soldCount: Int = 0,
    var sizes: List<String> = emptyList(),
    var color: String = "",
    var condition: String = "",
    var purchasedYear: Int = 0,
    var brand: String = "",
    var category: String = "",
    var status: String = "",

    var imageUrls: List<String> = emptyList(),

    var imageUrl: String = ""
) {
    fun getDisplayImage(): String {
        return if (imageUrls.isNotEmpty()) imageUrls[0] else imageUrl
    }
}