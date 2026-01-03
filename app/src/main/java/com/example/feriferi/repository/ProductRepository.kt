package com.example.feriferi.repository

class ProductRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val doc = db.collection("products").document()
        doc.set(product.copy(id = doc.id))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Failed to add product")
            }
    }

    fun updateProduct(
        productId: String,
        updatedData: Map<String, Any>,
        onSuccess: () -> Unit
    ) {
        db.collection("products")
            .document(productId)
            .update(updatedData)
            .addOnSuccessListener { onSuccess() }
    }
}
