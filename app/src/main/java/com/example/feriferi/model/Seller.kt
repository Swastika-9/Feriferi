package com.example.feriferi.model

data class Seller(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val profileImageUrl: String = "",
    val phone: String = "",
    val productsSold: Int = 0, // This will now come from "Orders", not "Products"
    val joiningDate: String = "Since 2026" // <--- NEW FIELD
)