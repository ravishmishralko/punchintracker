package com.office.punchintracker.data

/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 * Copyright (c) 2025 Ravish Mishra. All rights reserved.
 */

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Single DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

object DataStoreManager {
    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val IS_LOGGED_IN_KEY = stringPreferencesKey("is_logged_in")

    suspend fun saveUser(context: Context, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
            prefs[IS_LOGGED_IN_KEY] = "true"
        }
    }

    suspend fun logout(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN_KEY] = "false"
        }
    }

    fun getUserId(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_ID_KEY] ?: "User"
        }
    }

    fun isLoggedIn(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[IS_LOGGED_IN_KEY] == "true"
        }
    }
}