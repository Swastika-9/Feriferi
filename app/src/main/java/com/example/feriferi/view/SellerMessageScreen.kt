//package com.example.feriferi.view
//
//import android.os.Bundle
//import android.view.View
//import android.widget.FrameLayout
//import androidx.appcompat.app.AppCompatActivity
//import com.zegocloud.uikit.prebuilt.chat.ZegoUIKitPrebuiltChatFragment
//
//class SellerMessageScreen : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val container = FrameLayout(this)
//        container.id = View.generateViewId()
//        setContentView(container)
//
//        val sellerId = "seller1"
//        val sellerName = "Seller"
//        val buyerId = "buyer1"
//        val buyerName = "Buyer"
//
//        val fragment = ZegoUIKitPrebuiltChatFragment.newInstance(
//            appID = 1144419921L,
//            appSign = "50423a195e18a82ce2fb8aadabcadc5cfad4815ea9809ef57861800be296d9fc",
//            userID = sellerId,
//            userName = sellerName,
//            peerUserID = buyerId,
//            peerUserName = buyerName
//        )
//
//        supportFragmentManager.beginTransaction()
//            .replace(container.id, fragment)
//            .commit()
//    }
//}