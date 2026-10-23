package com.example.islamicguidance.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSettings(
    val language: AppLanguage = AppLanguage.BENGALI,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val arabicFontSize: Float = 28f,
    val translationFontSize: Float = 16f,
    val isHapticEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val selectedCityName: String = "ঢাকা",
    val lastReadSurah: Int = 1,
    val lastReadAyah: Int = 1,
    val bookmarks: Set<String> = emptySet(),
    val tasbihCount: Int = 0,
    val tasbihTarget: Int = 33,
    val tasbihDhikrName: String = "সুবহানাল্লাহ",
    val completedAzkarIds: Set<String> = emptySet(),
    val completedWorshipGoals: Set<String> = emptySet()
)

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("islamic_guidance_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private fun loadSettings(): UserSettings {
        val langCode = prefs.getString("language", "bn") ?: "bn"
        val lang = if (langCode == "en") AppLanguage.ENGLISH else AppLanguage.BENGALI

        val themeStr = prefs.getString("theme", "system") ?: "system"
        val theme = when (themeStr) {
            "light" -> ThemeMode.LIGHT
            "dark" -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }

        return UserSettings(
            language = lang,
            themeMode = theme,
            arabicFontSize = prefs.getFloat("arabic_font_size", 28f),
            translationFontSize = prefs.getFloat("translation_font_size", 16f),
            isHapticEnabled = prefs.getBoolean("haptic_enabled", true),
            isSoundEnabled = prefs.getBoolean("sound_enabled", true),
            selectedCityName = prefs.getString("selected_city", "ঢাকা") ?: "ঢাকা",
            lastReadSurah = prefs.getInt("last_read_surah", 1),
            lastReadAyah = prefs.getInt("last_read_ayah", 1),
            bookmarks = prefs.getStringSet("bookmarks", emptySet()) ?: emptySet(),
            tasbihCount = prefs.getInt("tasbih_count", 0),
            tasbihTarget = prefs.getInt("tasbih_target", 33),
            tasbihDhikrName = prefs.getString("tasbih_dhikr", "সুবহানাল্লাহ") ?: "সুবহানাল্লাহ",
            completedAzkarIds = prefs.getStringSet("completed_azkar", emptySet()) ?: emptySet(),
            completedWorshipGoals = prefs.getStringSet("completed_goals", emptySet()) ?: emptySet()
        )
    }

    fun setLanguage(lang: AppLanguage) {
        prefs.edit().putString("language", lang.code).apply()
        _settings.value = _settings.value.copy(language = lang)
    }

    fun setThemeMode(mode: ThemeMode) {
        val str = when (mode) {
            ThemeMode.LIGHT -> "light"
            ThemeMode.DARK -> "dark"
            ThemeMode.SYSTEM -> "system"
        }
        prefs.edit().putString("theme", str).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun setArabicFontSize(size: Float) {
        prefs.edit().putFloat("arabic_font_size", size).apply()
        _settings.value = _settings.value.copy(arabicFontSize = size)
    }

    fun setTranslationFontSize(size: Float) {
        prefs.edit().putFloat("translation_font_size", size).apply()
        _settings.value = _settings.value.copy(translationFontSize = size)
    }

    fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("haptic_enabled", enabled).apply()
        _settings.value = _settings.value.copy(isHapticEnabled = enabled)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
        _settings.value = _settings.value.copy(isSoundEnabled = enabled)
    }

    fun setSelectedCity(cityName: String) {
        prefs.edit().putString("selected_city", cityName).apply()
        _settings.value = _settings.value.copy(selectedCityName = cityName)
    }

    fun saveLastRead(surah: Int, ayah: Int) {
        prefs.edit().putInt("last_read_surah", surah).putInt("last_read_ayah", ayah).apply()
        _settings.value = _settings.value.copy(lastReadSurah = surah, lastReadAyah = ayah)
    }

    fun toggleBookmark(bookmarkKey: String) {
        val current = _settings.value.bookmarks.toMutableSet()
        if (current.contains(bookmarkKey)) {
            current.remove(bookmarkKey)
        } else {
            current.add(bookmarkKey)
        }
        prefs.edit().putStringSet("bookmarks", current).apply()
        _settings.value = _settings.value.copy(bookmarks = current)
    }

    fun isBookmarked(bookmarkKey: String): Boolean {
        return _settings.value.bookmarks.contains(bookmarkKey)
    }

    fun updateTasbih(count: Int, target: Int, dhikr: String) {
        prefs.edit()
            .putInt("tasbih_count", count)
            .putInt("tasbih_target", target)
            .putString("tasbih_dhikr", dhikr)
            .apply()
        _settings.value = _settings.value.copy(
            tasbihCount = count,
            tasbihTarget = target,
            tasbihDhikrName = dhikr
        )
    }

    fun toggleAzkarCompleted(azkarId: String) {
        val current = _settings.value.completedAzkarIds.toMutableSet()
        if (current.contains(azkarId)) {
            current.remove(azkarId)
        } else {
            current.add(azkarId)
        }
        prefs.edit().putStringSet("completed_azkar", current).apply()
        _settings.value = _settings.value.copy(completedAzkarIds = current)
    }

    fun toggleWorshipGoal(goalId: String) {
        val current = _settings.value.completedWorshipGoals.toMutableSet()
        if (current.contains(goalId)) {
            current.remove(goalId)
        } else {
            current.add(goalId)
        }
        prefs.edit().putStringSet("completed_goals", current).apply()
        _settings.value = _settings.value.copy(completedWorshipGoals = current)
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
        _settings.value = loadSettings()
    }
}
