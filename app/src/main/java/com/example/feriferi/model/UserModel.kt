package com.example.feriferi.model

data class UserModel(
    val userId: String = "",           // Firebase UID or custom ID
    val fullName: String = "",         // User's full name
    val email: String = "",            // Email (used in login/registration)
    val role: String = "",             // "Buyer" or "Seller" etc.
    val phoneNumber: String = "",      // Phone number
    val profileImageUrl: String = ""   // URL for profile picture
)
