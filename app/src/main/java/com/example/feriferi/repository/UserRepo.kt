package com.example.feriferi.repository

import com.example.feriferi.model.UserModel

interface UserRepo {

    fun register(
        email: String,
        password: String,
        user: UserModel,
        onResult: (Boolean, String) -> Unit
    )

    fun getCurrentUser(
        onSuccess: (UserModel) -> Unit,
        onFailure: (String) -> Unit
    )
}