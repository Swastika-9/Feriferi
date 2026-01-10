package com.example.feriferi.repository

import com.example.feriferi.model.ProductModel
import com.google.firebase.firestore.FirebaseFirestore

class ProductRepoImpl : ProductRepo {

    private val firestore = FirebaseFirestore.getInstance()
    private val productRef = firestore.collection("products")


    override fun addProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        val doc = productRef.document()
        val newProduct = model.copy(id = doc.id)

        doc.set(newProduct)
            .addOnSuccessListener {
                callback(true, "Product added successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to add product")
            }
    }

    override fun updateProduct(
        model: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        if (model.id.isEmpty()) {
            callback(false, "Product ID is missing")
            return
        }

        productRef.document(model.id)
            .set(model)
            .addOnSuccessListener {
                callback(true, "Product updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to update product")
            }
    }

    override fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        productRef.document(productId)
            .delete()
            .addOnSuccessListener {
                callback(true, "Product deleted successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to delete product")
            }
    }

    override fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    ) {
        productRef.document(productId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val product = snapshot.toObject(ProductModel::class.java)
                    callback(true, "Product fetched successfully", product)
                } else {
                    callback(false, "Product not found", null)
                }
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to fetch product", null)
            }
    }
    override fun getAllProduct(callback: (Boolean, String, List<ProductModel>?) -> Unit) {
        productRef.get()
            .addOnSuccessListener { snapshot ->
                val products = snapshot.documents.mapNotNull {
                    it.toObject(ProductModel::class.java)
                }
                callback(true, "Products fetched successfully", products)
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to fetch products", null)
            }
    }

    override fun getProductByCategory(
        categoryId: String,
        callback: (Boolean, String, List<ProductModel>?) -> Unit
    )  {
        productRef
            .whereEqualTo("category", categoryId)
            .get()
            .addOnSuccessListener { snapshot ->
                val products = snapshot.documents.mapNotNull {
                    it.toObject(ProductModel::class.java)
                }
                callback(true, "Category products fetched successfully", products)
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to fetch category products", null)
            }
    }


}
