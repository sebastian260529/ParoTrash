package com.example.parotrash.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "permisos")

class PermissionPreferences(private val context: Context) {

    companion object {
        private val UBICACION = booleanPreferencesKey("ubicacion")
        private val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        private val ALMACENAMIENTO = booleanPreferencesKey("almacenamiento")
    }

    val ubicacion: Flow<Boolean> = context.dataStore.data
        .map { it[UBICACION] ?: false }

    val notificaciones: Flow<Boolean> = context.dataStore.data
        .map { it[NOTIFICACIONES] ?: false }

    val almacenamiento: Flow<Boolean> = context.dataStore.data
        .map { it[ALMACENAMIENTO] ?: false }

    suspend fun setUbicacion(valor: Boolean) {
        context.dataStore.edit { it[UBICACION] = valor }
    }

    suspend fun setNotificaciones(valor: Boolean) {
        context.dataStore.edit { it[NOTIFICACIONES] = valor }
    }

    suspend fun setAlmacenamiento(valor: Boolean) {
        context.dataStore.edit { it[ALMACENAMIENTO] = valor }
    }
}