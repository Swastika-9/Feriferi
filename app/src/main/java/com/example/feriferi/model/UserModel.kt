package com.example.feriferi.model

data class UserModel(
    var userId: String = "",
    var fullName: String = "",
    var email: String = "",
    var username: String = "",
    var role: String = "",
    val profileImageUrl: String = "",
)