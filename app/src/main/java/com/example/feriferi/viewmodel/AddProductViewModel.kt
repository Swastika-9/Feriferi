package com.example.feriferi.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.feriferi.model.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore // Changed to Firestore

class AddProductViewModel : ViewModel() {

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    private val _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean> get() = _isUploading

    /**
     * Uploads multiple images to Cloudinary sequentially.
     * Once all are done, saves the product to Firestore.
     */
    fun uploadProductWithImages(product: ProductModel, imageUris: List<Uri>) {
        if (imageUris.size < 3) {
            _statusMessage.value = "Please select at least 3 images"
            return
        }

        _isUploading.value = true
        val uploadedUrls = mutableListOf<String>()
        var currentUploadIndex = 0

        fun uploadNext() {
            val uri = imageUris[currentUploadIndex]
            _statusMessage.postValue("Uploading image ${currentUploadIndex + 1}/${imageUris.size}...")

            MediaManager.get().upload(uri)
                .unsigned("feripheri_preset") // <--- MUST MATCH YOUR CLOUDINARY PRESET
                .option("folder", "products")
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {}
                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                        val imageUrl = resultData?.get("secure_url").toString()
                        uploadedUrls.add(imageUrl)
                        currentUploadIndex++

                        if (currentUploadIndex < imageUris.size) {
                            uploadNext() // Recursive call for next image
                        } else {
                            // All images uploaded, save to Firestore
                            saveToFirestore(product.copy(imageUrls = uploadedUrls))
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        _isUploading.postValue(false)
                        _statusMessage.postValue("Cloudinary Error: ${error?.description}")
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
                }).dispatch()
        }

        uploadNext()
    }

    private fun saveToFirestore(product: ProductModel) {
        val db = FirebaseFirestore.getInstance() // Use Firestore instance
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Ensure we have a valid user
        if (currentUser == null) {
            _isUploading.postValue(false)
            _statusMessage.postValue("Error: User not logged in")
            return
        }

        // Generate a new unique ID for the product
        val newProductRef = db.collection("products").document()
        val productId = newProductRef.id

        // Create the final product object with ID and SellerID
        val finalProduct = product.copy(
            id = productId,
            sellerId = currentUser.uid // Ensure the seller ID is correct
        )

        // Save to the "products" collection in Firestore
        newProductRef.set(finalProduct)
            .addOnSuccessListener {
                _isUploading.postValue(false)
                _statusMessage.postValue("Product Added Successfully!")
            }
            .addOnFailureListener { e ->
                _isUploading.postValue(false)
                _statusMessage.postValue("Firestore Error: ${e.message}")
            }
    }
}