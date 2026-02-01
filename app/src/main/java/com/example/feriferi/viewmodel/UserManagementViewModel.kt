package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.UserModel
import com.example.feriferi.repository.UserRepository
import com.example.feriferi.repository.UserRepoImpl
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserManagementViewModel(
    private val repository: UserRepository
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

    // This function uses Coroutines because 'getCurrentUser' is a suspend function
    private fun loadUser() {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Returns UserModel?, so we assign it directly
                val result = repository.getCurrentUser()
                _user.value = result
            } catch (e: Exception) {
                _message.value = e.message ?: "Failed to load user"
            } finally {
                _loading.value = false
            }
        }
    }

    // These functions still use Callbacks (onSuccess/onError) based on your error logs
    fun verifyUser() {
        val uid = _user.value?.userId ?: return
        _loading.value = true

        repository.verifyUser(
            uid, // Assuming only UID is passed as the first arg
            onSuccess = {
                _message.value = "User verified successfully"
                _loading.value = false
            },
            onError = { errorMsg ->
                _message.value = errorMsg
                _loading.value = false
            }
        )
    }

    fun removeUser() {
        val uid = _user.value?.userId ?: return
        _loading.value = true

        repository.removeUser(
            uid,
            onSuccess = {
                _message.value = "User removed successfully"
                _user.value = null
                _loading.value = false
            },
            onError = { errorMsg ->
                _message.value = errorMsg
                _loading.value = false
            }
        )
    }

    fun changePassword(newPassword: String) {
        _loading.value = true

        repository.changePassword(
            newPassword,
            onSuccess = {
                _message.value = "Password changed successfully"
                _loading.value = false
            },
            onError = { errorMsg ->
                _message.value = errorMsg
                _loading.value = false
            }
        )
    }

    fun clearMessage() {
        _message.value = null
    }
}