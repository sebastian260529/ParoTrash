package com.example.parotrash.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notificaciones")

class NotificationPreferences(private val context: Context) {

    companion object {
        private val RECIBIR_ALERTAS = booleanPreferencesKey("recibir_alertas")
        private val BLOQUEOS_VIALES = booleanPreferencesKey("bloqueos_viales")
        private val MANIFESTACIONES = booleanPreferencesKey("manifestaciones")
        private val RUTAS_ALTERNATIVAS = booleanPreferencesKey("rutas_alternativas")
        private val COMUNIDAD = booleanPreferencesKey("comunidad")
    }

    // Observar estados
    val recibirAlertas: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[RECIBIR_ALERTAS] ?: true }

    val bloqueosViales: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[BLOQUEOS_VIALES] ?: true }

    val manifestaciones: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[MANIFESTACIONES] ?: true }

    val rutasAlternativas: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[RUTAS_ALTERNATIVAS] ?: true }

    val comunidad: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[COMUNIDAD] ?: true }

    // Guardar estados
    suspend fun setRecibirAlertas(valor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[RECIBIR_ALERTAS] = valor
        }
    }

    suspend fun setBloqueosViales(valor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BLOQUEOS_VIALES] = valor
        }
    }

    suspend fun setManifestaciones(valor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[MANIFESTACIONES] = valor
        }
    }

    suspend fun setRutasAlternativas(valor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[RUTAS_ALTERNATIVAS] = valor
        }
    }

    suspend fun setComunidad(valor: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COMUNIDAD] = valor
        }
    }
}