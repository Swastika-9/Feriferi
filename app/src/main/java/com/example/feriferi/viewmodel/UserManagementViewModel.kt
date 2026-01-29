package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.feriferi.model.User
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserManagementViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadUser()
    }
    private fun loadUser() {
        _loading.value = true
        repository.getCurrentUserWithCallback(
            onSuccess = {
                _user.value = it
                _loading.value = false
            },
            onError = {
                _message.value = it
                _loading.value = false
            }
        )
    }

    fun verifyUser() {
        val uid = _user.value?.userId ?: return
        _loading.value = true

        repository.verifyUser(
            uid = uid,
            onSuccess = {
                _message.value = "User verified successfully"
                _loading.value = false
            },
            onError = {
                _message.value = it
                _loading.value = false
            }
        )
    }

    fun removeUser() {
        val uid = _user.value?.userId ?: return
        _loading.value = true

        repository.removeUser(
            uid = uid,
            onSuccess = {
                _message.value = "User removed successfully"
                _user.value = null
                _loading.value = false
            },
            onError = {
                _message.value = it
                _loading.value = false
            }
        )
    }

    fun changePassword(newPassword: String) {
        _loading.value = true

        repository.changePasswordUnsafe(
            newPassword = newPassword,
            onSuccess = {
                _message.value = "Password changed successfully"
                _loading.value = false
            },
            onError = {
                _message.value = it
                _loading.value = false
            }
        )
    }

    fun clearMessage() {
        _message.value = null
    }
}
