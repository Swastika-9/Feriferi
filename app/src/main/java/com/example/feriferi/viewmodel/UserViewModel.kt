package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepository

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    fun registerUser(
        email: String,
        password: String,
        user: UserModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {

            val result = repo.registerUser(email, password, user)

            if (result.first) {
                onSuccess()
            } else {
                onFailure(result.second)
            }
        }
    }
}