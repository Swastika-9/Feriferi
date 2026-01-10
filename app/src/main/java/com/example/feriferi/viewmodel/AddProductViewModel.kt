//package com.example.feriferi.viewmodel
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import com.example.feriferi.model.ProductModel
//import com.example.feriferi.repository.ProductRepository
//
//class AddProductViewModel : ViewModel() {
//
//    private val repo = ProductRepository()
//
//    var name by mutableStateOf("")
//    var price by mutableStateOf("")
//    var originalPrice by mutableStateOf("")
//    var size by mutableStateOf("M")
//    var description by mutableStateOf("")
//    var color by mutableStateOf("")
//    var condition by mutableStateOf("")
//    var timesWorn by mutableStateOf("")
//    var company by mutableStateOf("")
//    var tag by mutableStateOf("")
//    var mainCategory by mutableStateOf("Clothing")
//    var subCategory by mutableStateOf("Kids")
//    var status by mutableStateOf("Available")
//
//    fun addProduct(
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        val product = ProductModel(
//            name = name,
//            price = price.toDouble(),
//            originalPrice = originalPrice.toDouble(),
//            size = size,
//            description = description,
//            color = color,
//            condition = condition,
//            timesWorn = timesWorn.toInt(),
//            company = company,
//            tag = tag,
//            mainCategory = mainCategory,
//            subCategory = subCategory,
//            status = status
//        )
//
//        repo.addProduct(product, onSuccess, onError)
//    }
//}
