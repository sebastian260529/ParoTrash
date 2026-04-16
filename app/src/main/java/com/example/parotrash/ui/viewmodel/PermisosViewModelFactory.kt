package com.example.parotrash.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parotrash.data.PermissionPreferences

class PermisosViewModelFactory(
    private val preferences: PermissionPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PermisosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PermisosViewModel(preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}