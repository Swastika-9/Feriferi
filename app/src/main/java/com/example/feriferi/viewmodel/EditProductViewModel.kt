package com.example.feriferi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.feriferi.model.ProductModel

class EditProductViewModel : ViewModel() {

    var productState by mutableStateOf(ProductModel())
        private set

    fun initializeProduct(product: ProductModel) {
        productState = product
    }

    fun updateColor(newColor: String) {
        productState = productState.copy(color = newColor)
    }

    fun updateCondition(newCondition: String) {
        productState = productState.copy(condition = newCondition)
    }

    fun updateTimesWorn(newTimes: String) {
        productState = productState.copy(timesWorn = newTimes)
    }

    fun updateBrand(newBrand: String) {
        productState = productState.copy(brand = newBrand)
    }

    fun updateCategory(newCategory: String) {
        productState = productState.copy(category = newCategory)
    }

    fun saveChanges(onSuccess: () -> Unit) {
        // Here you would typically perform a Repository/Firebase update
        onSuccess()
    }

    fun saveProduct(onSuccess: (ProductModel) -> Unit) {
        // Here you would typically perform a Repository/Firebase update
        onSuccess(productState)
    }
}