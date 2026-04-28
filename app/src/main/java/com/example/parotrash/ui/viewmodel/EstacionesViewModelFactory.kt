package com.example.parotrash.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EstacionesViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstacionesViewModel::class.java)) {
            return EstacionesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}