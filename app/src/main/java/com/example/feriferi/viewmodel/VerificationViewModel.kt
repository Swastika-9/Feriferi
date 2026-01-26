package com.example.feriferi

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VerificationViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<Result<String>?>(null)
    val state: StateFlow<Result<String>?> = _state

    // This must be saved when OTP is sent
    var verificationId: String? = null

    fun verifyCode(c1: String, c2: String, c3: String, c4: String) {

        val code = c1 + c2 + c3 + c4

        if (code.length != 4) {
            _state.value = Result.failure(Exception("Enter complete code"))
            return
        }

        val id = verificationId
        if (id == null) {
            _state.value = Result.failure(Exception("Verification expired"))
            return
        }

        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(id, code)

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _state.value = Result.success("Verification successful")
            }
            .addOnFailureListener { e ->
                _state.value = Result.failure(e)
            }
    }
}
