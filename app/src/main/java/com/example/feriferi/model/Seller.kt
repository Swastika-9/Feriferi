package com.example.feriferi.model

data class Seller(
    val name: String,
    val username: String,
    val profileImage: Int,  // use Int for drawable
    val productsSold: Int
)
