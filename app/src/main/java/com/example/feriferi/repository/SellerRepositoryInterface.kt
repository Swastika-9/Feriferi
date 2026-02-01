package com.example.feriferi.repository

import com.example.feriferi.model.Seller

interface SellerRepositoryInterface {
    fun getSellerData(uid: String, callback: (Boolean, String, Seller?) -> Unit)
    fun updateSellerProfile(uid: String, updates: Map<String, Any>, callback: (Boolean, String) -> Unit)
}