package com.babynumbers.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.babynumbers.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

@Singleton
class LearningRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val LANGUAGE = stringPreferencesKey(Constants.KEY_LANGUAGE)
        val CURRENT_STAGE = intPreferencesKey(Constants.KEY_CURRENT_STAGE)
        val COMPLETED_NUMBERS = stringSetPreferencesKey(Constants.KEY_COMPLETED_NUMBERS)
    }

    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.LANGUAGE] ?: Constants.LANGUAGE_CHINESE
    }

    val currentStage: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[Keys.CURRENT_STAGE] ?: 1
    }

    val completedNumbers: Flow<Set<Int>> = context.dataStore.data.map { preferences ->
        preferences[Keys.COMPLETED_NUMBERS]?.map { it.toInt() }?.toSet() ?: emptySet()
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.LANGUAGE] = language
        }
    }

    suspend fun setCurrentStage(stage: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CURRENT_STAGE] = stage
        }
    }

    suspend fun markNumberCompleted(number: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[Keys.COMPLETED_NUMBERS]?.toMutableSet() ?: mutableSetOf()
            current.add(number.toString())
            preferences[Keys.COMPLETED_NUMBERS] = current
        }
    }

    suspend fun isNumberCompleted(number: Int): Boolean {
        val completed = completedNumbers.first()
        return completed.contains(number)
    }

    suspend fun getCompletedCountForStage(stage: Int): Int {
        val completed = completedNumbers.first()
        return when (stage) {
            1 -> completed.filter { it in 1..10 }.size
            2 -> completed.filter { it in 11..20 }.size
            3 -> completed.filter { it in 21..50 }.size
            4 -> completed.filter { it in 51..100 }.size
            else -> 0
        }
    }

    suspend fun isStageUnlocked(stage: Int): Boolean {
        if (stage == 1) return true
        val prevStage = stage - 1
        val completedCount = getCompletedCountForStage(prevStage)
        val totalCount = when (prevStage) {
            1 -> 10
            2 -> 10
            3 -> 30
            4 -> 50
            else -> 0
        }
        return completedCount >= totalCount
    }
}
