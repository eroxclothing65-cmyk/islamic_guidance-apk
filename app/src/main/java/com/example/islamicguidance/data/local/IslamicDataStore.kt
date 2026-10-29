package com.example.islamicguidance.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.islamicguidance.data.AppLanguage
import com.example.islamicguidance.data.ThemeMode
import com.example.islamicguidance.data.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "islamic_guidance_datastore")

class IslamicDataStore(private val context: Context) {

    private object PreferencesKeys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ARABIC_FONT_SIZE = floatPreferencesKey("arabic_font_size")
        val TRANSLATION_FONT_SIZE = floatPreferencesKey("translation_font_size")
        val HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val SELECTED_CITY = stringPreferencesKey("selected_city")
        val TASBIH_COUNT = intPreferencesKey("tasbih_count")
        val TASBIH_TARGET = intPreferencesKey("tasbih_target")
        val TASBIH_DHIKR = stringPreferencesKey("tasbih_dhikr")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val langCode = preferences[PreferencesKeys.LANGUAGE] ?: "bn"
            val lang = if (langCode == "en") AppLanguage.ENGLISH else AppLanguage.BENGALI

            val themeStr = preferences[PreferencesKeys.THEME_MODE] ?: "system"
            val theme = when (themeStr) {
                "light" -> ThemeMode.LIGHT
                "dark" -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }

            UserSettings(
                language = lang,
                themeMode = theme,
                arabicFontSize = preferences[PreferencesKeys.ARABIC_FONT_SIZE] ?: 28f,
                translationFontSize = preferences[PreferencesKeys.TRANSLATION_FONT_SIZE] ?: 16f,
                isHapticEnabled = preferences[PreferencesKeys.HAPTIC_ENABLED] ?: true,
                isSoundEnabled = preferences[PreferencesKeys.SOUND_ENABLED] ?: true,
                selectedCityName = preferences[PreferencesKeys.SELECTED_CITY] ?: "ঢাকা",
                tasbihCount = preferences[PreferencesKeys.TASBIH_COUNT] ?: 0,
                tasbihTarget = preferences[PreferencesKeys.TASBIH_TARGET] ?: 33,
                tasbihDhikrName = preferences[PreferencesKeys.TASBIH_DHIKR] ?: "সুবহানাল্লাহ"
            )
        }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = language.code
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = when (mode) {
                ThemeMode.LIGHT -> "light"
                ThemeMode.DARK -> "dark"
                ThemeMode.SYSTEM -> "system"
            }
        }
    }

    suspend fun setArabicFontSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ARABIC_FONT_SIZE] = size
        }
    }

    suspend fun setTranslationFontSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TRANSLATION_FONT_SIZE] = size
        }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTIC_ENABLED] = enabled
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun setSelectedCity(cityName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CITY] = cityName
        }
    }

    suspend fun updateTasbih(count: Int, target: Int, dhikr: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TASBIH_COUNT] = count
            preferences[PreferencesKeys.TASBIH_TARGET] = target
            preferences[PreferencesKeys.TASBIH_DHIKR] = dhikr
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
