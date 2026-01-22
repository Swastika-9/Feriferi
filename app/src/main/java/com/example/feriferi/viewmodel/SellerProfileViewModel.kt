package com.example.feriferi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.feriferi.model.Seller
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SellerProfileViewModel : ViewModel() {
    private val _seller = MutableStateFlow(
        Seller("Vivienne Shirley", "@vivienne", com.example.feriferi.R.drawable.seller_profile)
    )
    val seller: StateFlow<Seller> = _seller.asStateFlow()

    fun onNameChange(newName: String) {
        _seller.value = _seller.value.copy(name = newName)
    }

    fun onUsernameChange(newUsername: String) {
        _seller.value = _seller.value.copy(username = newUsername)
    }

    fun saveSellerProfile(newName: String, newUsername: String) {
        _seller.value = _seller.value.copy(name = newName, username = newUsername)
    }
}