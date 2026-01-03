package com.example.feriferi.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.feriferi.R
import com.example.feriferi.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.Locale

class BuyerCartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    val deliveryFee = 70.0

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private var cartRef: DatabaseReference? = null

    init {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            cartRef = database.getReference("carts/$uid")
            observeCart()
        } else {
            // For demo: add sample items if no user logged in
            _cartItems.addAll(
                listOf(
                    CartItem("1", "YSL Men's High Heels Boot", 590.0, R.drawable.menhighheels),
                    CartItem("2", "Sun Glass Dior", 650.0, R.drawable.sunglass),
                    CartItem("3", "Berkin 20 Mat Alligator", 450.0, R.drawable.berkin),
                    CartItem("4", "Jacket H&M", 790.0, R.drawable.hmjacket)
                )
            )
        }
    }

    /** ----------------- Firebase Observing ----------------- **/
    private fun observeCart() {
        cartRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _cartItems.clear()
                snapshot.children.forEach { itemSnap ->
                    val cartItem = itemSnap.getValue(CartItem::class.java)
                    cartItem?.let { _cartItems.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

    /** ----------------- Cart Operations ----------------- **/
    fun increaseQuantity(item: CartItem) {
        val index = _cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _cartItems[index].quantity += 1
            syncCartWithFirebase()
        }
    }

    fun decreaseQuantity(item: CartItem) {
        val index = _cartItems.indexOfFirst { it.id == item.id }
        if (index != -1 && _cartItems[index].quantity > 1) {
            _cartItems[index].quantity -= 1
            syncCartWithFirebase()
        }
    }

    fun removeItem(item: CartItem) {
        _cartItems.removeIf { it.id == item.id }
        syncCartWithFirebase()
    }

    fun removeItems(items: List<CartItem>) {
        _cartItems.removeAll(items)
        syncCartWithFirebase()
    }

    /** ----------------- Firebase Sync ----------------- **/
    fun syncCartWithFirebase() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        cartRef = database.getReference("carts/$uid")
        cartRef?.setValue(_cartItems)
    }

    /** ----------------- Price Calculations ----------------- **/
    fun getSubtotal(): Double = _cartItems.sumOf { it.price * it.quantity }

    fun getTotal(): Double = getSubtotal() + deliveryFee

    /** ----------------- Price Formatting ----------------- **/
    private fun formatPrice(price: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "NP"))
        formatter.maximumFractionDigits = 0
        return formatter.format(price)
    }

    fun getFormattedPrice(item: CartItem): String = formatPrice(item.price * item.quantity)

    fun getFormattedSubtotal(): String = formatPrice(getSubtotal())

    fun getFormattedDelivery(): String = formatPrice(deliveryFee)

    fun getFormattedTotal(): String = formatPrice(getTotal())
}
