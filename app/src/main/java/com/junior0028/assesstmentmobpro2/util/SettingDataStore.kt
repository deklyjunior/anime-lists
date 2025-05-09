package com.junior0028.assesstmentmobpro2.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingDataStore(private val context: Context) {
    companion object {
        val IS_GRID = booleanPreferencesKey("is_grid")
    }

    val layoutFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_GRID] == true
    }

    suspend fun saveLayout(isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_GRID] = isGrid
        }
    }
}