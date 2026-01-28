package com.example.feriferi.model

data class Product(
    val name: String,
    val color: String,
    val condition: String,
    val timesWorn: Int,
    val company: String,
    val tag: String,
    val imageRes: Int,
    val isSold: Boolean = false
)
