package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepository

// 1. Extend 'ViewModel()' to get access to 'viewModelScope'
class UserViewModel(private val repo: UserRepository) : ViewModel() {

    fun registerUser(
        email: String,
        password: String,
        user: UserModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // 2. Launch a Coroutine because 'repo.registerUser' is a suspend function
        viewModelScope.launch {
            // 3. Call the single function that does BOTH Auth and Database work
            val result = repo.registerUser(email, password, user)

            // result is a Pair<Boolean, String> -> (Success?, Message)
            if (result.first) {
                onSuccess()
            } else {
                onFailure(result.second)
            }
        }
    }
}