package com.example.feriferi.model

data class Seller(
    val name: String,
    val username: String,
    val profileImage: Int,
    val profileImageUrl: String? = null,
    val productsSold: Int = 0
)
