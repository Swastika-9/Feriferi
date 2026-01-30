package com.example.feriferi.repository

import android.net.Uri
import com.example.feriferi.model.UserModel

interface UserRepository {
    // Auth & Database
    suspend fun registerUser(email: String, password: String, user: UserModel): Pair<Boolean, String>
    suspend fun loginUser(email: String, password: String): Pair<Boolean, String>
    suspend fun getCurrentUser(): UserModel?
    fun logout()

    // Images
    suspend fun uploadProfileImage(imageUri: Uri): String?

    // Management (Admin)
    fun verifyUser(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun removeUser(uid: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun changePassword(newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit)
    fun updateSellerProfile(uid: String, updates: Map<String, Any>, callback: (Boolean, String) -> Unit)
}