package com.example.feriferi.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val image: Int,
    var quantity: MutableState<Int> = mutableStateOf(1)
)
