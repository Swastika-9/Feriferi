package com.example.feriferi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.feriferi.model.Seller

class SellerProfileViewModel : ViewModel() {
    var sellerState by mutableStateOf(
        Seller("Vivienne Shirley", "@vivienne", com.example.feriferi.R.drawable.seller_profile)
    )
        private set

    fun onNameChange(newName: String) {
        sellerState = sellerState.copy(name = newName)
    }

    fun onUsernameChange(newUsername: String) {
        sellerState = sellerState.copy(username = newUsername)
    }

    fun saveProfile() {
    }
}