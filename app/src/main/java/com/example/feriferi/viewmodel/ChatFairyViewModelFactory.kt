package com.example.feriferi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feriferi.repository.ChatFairyRepo

class ChatFairyViewModelFactory(private val repo: ChatFairyRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatFairyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatFairyViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}