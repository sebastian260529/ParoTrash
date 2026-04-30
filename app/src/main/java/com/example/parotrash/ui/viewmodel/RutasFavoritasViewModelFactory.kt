package com.example.parotrash.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RutasFavoritasViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RutasFavoritasViewModel::class.java)) {
            return RutasFavoritasViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}