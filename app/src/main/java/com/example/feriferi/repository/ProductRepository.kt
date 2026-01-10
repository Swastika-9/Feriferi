package com.example.feriferi.repository


import com.example.feriferi.model.ProductModel

interface ProductRepo {

    // ADD PRODUCT
    fun addProduct(
        model: ProductModel,
        callback: (success: Boolean, message: String) -> Unit
    )

    // UPDATE PRODUCT
    fun updateProduct(
        model: ProductModel,
        callback: (success: Boolean, message: String) -> Unit
    )

    // DELETE PRODUCT
    fun deleteProduct(
        productId: String,
        callback: (success: Boolean, message: String) -> Unit
    )

    // GET PRODUCT BY ID
    fun getProductById(
        productId: String,
        callback: (success: Boolean, message: String, product: ProductModel?) -> Unit
    )

    // GET ALL PRODUCTS
    fun getAllProduct(
        callback: (success: Boolean, message: String, products: List<ProductModel>?) -> Unit
    )

    // GET PRODUCTS BY CATEGORY
    fun getProductByCategory(
        categoryId: String,
        callback: (success: Boolean, message: String, products: List<ProductModel>?) -> Unit
    )
}
