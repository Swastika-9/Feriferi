package com.example.feriferi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feriferi.model.ProductModel
import com.google.firebase.database.*

data class UserAccount(
    val id: String = "",
    val name: String = "",
    val role: String = "buyer",
    val isBanned: Boolean = false
)

class AdminDashboardViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()

    private val _users = MutableLiveData<List<UserAccount>>(emptyList())
    val users: LiveData<List<UserAccount>> get() = _users

    private val _products = MutableLiveData<List<ProductModel>>(emptyList())
    val products: LiveData<List<ProductModel>> get() = _products


    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchAllUsers()
        fetchAllProducts()
    }

    private fun fetchAllUsers() {
        _isLoading.postValue(true)

        db.getReference("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserAccount>()
                for (userSnap in snapshot.children) {
                    try {
                        val user = userSnap.getValue(UserAccount::class.java)
                        user?.let { userList.add(it.copy(id = userSnap.key ?: "")) }
                    } catch (e: Exception) {
                        Log.e("AdminVM", "Error parsing user ${userSnap.key}: ${e.message}")
                    }
                }
                _users.postValue(userList)
                _isLoading.postValue(false)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Users fetch cancelled: ${error.message}")
                _isLoading.postValue(false)
            }
        })
    }

    private fun fetchAllProducts() {
        db.getReference("Products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductModel>()
                for (prodSnap in snapshot.children) {
                    try {
                        val product = prodSnap.getValue(ProductModel::class.java)
                        product?.let { productList.add(it) }
                    } catch (e: Exception) {
                        Log.e("AdminVM", "Error parsing product ${prodSnap.key}: ${e.message}")
                    }
                }
                _products.postValue(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Products fetch cancelled: ${error.message}")
            }
        })
    }

    fun toggleUserBan(userId: String, shouldBan: Boolean) {
        db.getReference("Users").child(userId).child("isBanned").setValue(shouldBan)
    }

    fun deleteProduct(productId: String) {
        db.getReference("Products").child(productId).removeValue()
    }
}
