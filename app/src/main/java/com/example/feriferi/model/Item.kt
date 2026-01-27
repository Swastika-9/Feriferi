package com.example.feriferi.model

data class Item(
    var name: String = "",
    var price: Int = 0,
    var originalPrice: Int = 0,
    var sellerName: String = "",
    var soldCount: Int = 0,
    var sizes: List<String> = emptyList(),
    var color: String = "",
    var condition: String = "",
    var purchasedYear: Int = 0,
    var brand: String = "",
    var category: String = "",
    var status: String = ""
)
