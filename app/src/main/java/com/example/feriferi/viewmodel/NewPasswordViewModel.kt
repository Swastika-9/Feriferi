package com.example.feriferi

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewPasswordViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<Result<String>?>(null)
    val state: StateFlow<Result<String>?> = _state

    fun updatePassword(password: String, confirmPassword: String) {

        if (password.length < 6) {
            _state.value = Result.failure(Exception("Password must be at least 6 characters"))
            return
        }

        if (password != confirmPassword) {
            _state.value = Result.failure(Exception("Passwords do not match"))
            return
        }

        val user = auth.currentUser

        if (user == null) {
            _state.value = Result.failure(Exception("User not logged in"))
            return
        }

        user.updatePassword(password)
            .addOnSuccessListener {
                _state.value = Result.success("Password updated successfully")
            }
            .addOnFailureListener { e ->
                _state.value = Result.failure(e)
            }
    }
}
