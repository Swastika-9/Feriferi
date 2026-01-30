package com.example.feriferi.model

data class Seller(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val profileImageUrl: Any? = null, // Can be String (URL) or Int (R.drawable.xxx)
    val productsSold: Int = 0,
    val phone: String = "" // <--- ADD THIS FIELD
)