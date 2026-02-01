package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feriferi.model.LikedProducts
import com.example.feriferi.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FavouriteViewModel : ViewModel() {

    private val repository = FavoriteRepository()

    private val _favorites = MutableStateFlow<List<LikedProducts>>(emptyList())
    val favorites: StateFlow<List<LikedProducts>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = repository.getFavorites()
        }
    }
}
