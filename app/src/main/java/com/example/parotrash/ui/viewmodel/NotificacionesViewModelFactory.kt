package com.example.parotrash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parotrash.data.NotificationPreferences

class NotificacionesViewModelFactory(
    private val preferences: NotificationPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificacionesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotificacionesViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}