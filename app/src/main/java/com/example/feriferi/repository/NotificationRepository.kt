package com.example.feriferi.repository

import com.example.feriferi.model.NotificationModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object NotificationRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    // 1. SEND A NOTIFICATION (Updated with actionable fields)
    fun sendNotification(
        targetUserId: String,
        title: String,
        message: String,
        type: String,
        senderId: String = "",
        productId: String = "",
        productImage: String = "",
        offerPrice: Double = 0.0
    ) {
        if (targetUserId.isEmpty()) return

        val ref = db.reference.child("Notifications").child(targetUserId)
        val notifId = ref.push().key ?: return

        val notification = NotificationModel(
            id = notifId,
            title = title,
            message = message,
            timestamp = System.currentTimeMillis(),
            type = type, // "offer", "order_request", "general"
            read = false, // Default is unread
            senderId = senderId,
            productId = productId,
            productImage = productImage,
            offerPrice = offerPrice
        )

        ref.child(notifId).setValue(notification)
    }

    // 2. GET UNREAD COUNT (Fixes 'Unresolved reference getUnreadCount')
    fun getUnreadCount(onCountChange: (Int) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.reference.child("Notifications").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var count = 0
                for (child in snapshot.children) {
                    val notif = child.getValue(NotificationModel::class.java)
                    // Count if notification exists and is NOT read
                    if (notif != null && !notif.read) {
                        count++
                    }
                }
                onCountChange(count)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 3. MARK ALL AS READ (Fixes 'Unresolved reference markAllAsRead')
    fun markAllAsRead() {
        val userId = auth.currentUser?.uid ?: return
        val ref = db.reference.child("Notifications").child(userId)

        // Fetch once and update 'read' status
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val notif = child.getValue(NotificationModel::class.java)
                    if (notif != null && !notif.read) {
                        child.ref.child("read").setValue(true)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 4. GET ALL NOTIFICATIONS (For Notification Screen)
    fun getNotifications(onData: (List<NotificationModel>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.reference.child("Notifications").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<NotificationModel>()
                    for (child in snapshot.children) {
                        val item = child.getValue(NotificationModel::class.java)
                        if (item != null) list.add(item)
                    }
                    // Sort by newest first
                    onData(list.sortedByDescending { it.timestamp })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // 5. DELETE NOTIFICATION
    fun deleteNotification(notificationId: String) {
        val userId = auth.currentUser?.uid ?: return
        db.reference.child("Notifications").child(userId).child(notificationId).removeValue()
    }

    // --- ACTIONS (Accept/Reject Logic) ---

    fun acceptOffer(notification: NotificationModel) {
        // Notify Buyer
        sendNotification(
            targetUserId = notification.senderId,
            title = "Offer Accepted!",
            message = "Seller accepted your offer of Rs ${notification.offerPrice}. You can now buy it.",
            type = "general",
            productId = notification.productId,
            productImage = notification.productImage
        )
        // Remove the request
        deleteNotification(notification.id)
    }

    fun rejectOffer(notification: NotificationModel) {
        sendNotification(
            targetUserId = notification.senderId,
            title = "Offer Declined",
            message = "Seller declined your offer of Rs ${notification.offerPrice}.",
            type = "general",
            productId = notification.productId,
            productImage = notification.productImage
        )
        deleteNotification(notification.id)
    }

    fun acceptOrder(notification: NotificationModel) {
        sendNotification(
            targetUserId = notification.senderId,
            title = "Order Confirmed!",
            message = "Your order has been accepted and is being processed.",
            type = "general",
            productId = notification.productId,
            productImage = notification.productImage
        )
        deleteNotification(notification.id)
    }

    fun rejectOrder(notification: NotificationModel) {
        sendNotification(
            targetUserId = notification.senderId,
            title = "Order Cancelled",
            message = "Sorry, the seller cancelled your order.",
            type = "general",
            productId = notification.productId,
            productImage = notification.productImage
        )
        deleteNotification(notification.id)
    }
}