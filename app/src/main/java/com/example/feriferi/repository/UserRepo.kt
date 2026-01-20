package com.example.feriferi.repository

import com.example.feriferi.model.UserModel

interface UserRepo {
    fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit)
    fun addUserToDatabase(userId: String, user: UserModel, callback: (Boolean, String) -> Unit)
}