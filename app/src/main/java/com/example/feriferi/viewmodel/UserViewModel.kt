package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
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
        repo.registerUser(
            email = email,
            password = password,
            user = user,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}