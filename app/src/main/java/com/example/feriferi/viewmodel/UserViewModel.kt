package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepo

class UserViewModel(private val userRepo: UserRepo) : ViewModel() {

    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        userRepo.register(email, password, callback)
    }

    fun addUserToDatabase(userId: String, user: UserModel, callback: (Boolean, String) -> Unit) {
        userRepo.addUserToDatabase(userId, user, callback)
    }
}