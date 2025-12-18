package com.example.feriferi.viewmodel

import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepo

class UserViewModel(private val repo: UserRepo) {

    fun registerUser(
        email: String,
        password: String,
        user: UserModel,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        repo.register(email, password) { success, message, userId ->
            if (success) {
                repo.addUserToDatabase(userId, user) { dbSuccess, dbMessage ->
                    if (dbSuccess) {
                        onSuccess()
                    } else {
                        onFailure(dbMessage)
                    }
                }
            } else {
                onFailure(message)
            }
        }
    }
}