package com.example.parotrash.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion")

class SessionManager(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    val isFirstTime: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_FIRST_TIME] ?: true
        }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun setFirstTimeDone() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = false
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
        }
    }
}