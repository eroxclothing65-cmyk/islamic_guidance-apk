package com.example.islamicguidance.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.islamicguidance.data.*
import com.example.islamicguidance.service.CompassSensorManager
import com.example.islamicguidance.service.QuranAudioPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class IslamicViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context get() = getApplication<Application>().applicationContext
    val prefsRepo = UserPreferencesRepository(context)
    val audioPlayer = QuranAudioPlayer(context)
    val compassSensor = CompassSensorManager(context)

    val settings: StateFlow<UserSettings> = prefsRepo.settings

    private val _selectedSurah = MutableStateFlow<SurahInfo?>(null)
    val selectedSurah: StateFlow<SurahInfo?> = _selectedSurah.asStateFlow()

    private val _surahVerses = MutableStateFlow<List<Ayah>>(emptyList())
    val surahVerses: StateFlow<List<Ayah>> = _surahVerses.asStateFlow()

    private val _prayerSchedule = MutableStateFlow<PrayerSchedule?>(null)
    val prayerSchedule: StateFlow<PrayerSchedule?> = _prayerSchedule.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedHadithCategory = MutableStateFlow<HadithCategory?>(null)
    val selectedHadithCategory: StateFlow<HadithCategory?> = _selectedHadithCategory.asStateFlow()

    private val _selectedAzkarCategory = MutableStateFlow(AzkarCategory.MORNING)
    val selectedAzkarCategory: StateFlow<AzkarCategory> = _selectedAzkarCategory.asStateFlow()

    private val _selectedDuaCategory = MutableStateFlow<DuaCategory?>(null)
    val selectedDuaCategory: StateFlow<DuaCategory?> = _selectedDuaCategory.asStateFlow()

    // Worship goals
    val worshipGoals = listOf(
        WorshipGoalItem("goal_fajr", "ফজর নামাজ আদায়", "Fajr Prayer offered"),
        WorshipGoalItem("goal_dhuhr", "যোহর নামাজ আদায়", "Dhuhr Prayer offered"),
        WorshipGoalItem("goal_asr", "আসর নামাজ আদায়", "Asr Prayer offered"),
        WorshipGoalItem("goal_maghrib", "মাগরিব নামাজ আদায়", "Maghrib Prayer offered"),
        WorshipGoalItem("goal_isha", "ইশা নামাজ আদায়", "Isha Prayer offered"),
        WorshipGoalItem("goal_quran", "দৈনিক কুরআন তেলাওয়াত ও তাদাব্বুর", "Daily Quran Recitation"),
        WorshipGoalItem("goal_morning_azkar", "সকালের মাসনূন আজকার পাঠ", "Morning Azkar completed"),
        WorshipGoalItem("goal_evening_azkar", "সন্ধ্যার মাসনূন আজকার পাঠ", "Evening Azkar completed"),
        WorshipGoalItem("goal_istighfar", "১০০ বার ইস্তিগফার পাঠ", "100x Istighfar recited"),
        WorshipGoalItem("goal_mulk", "রাতে সূরা আল-মুলক তেলাওয়াত", "Recite Surah Al-Mulk at night")
    )

    init {
        // Start prayer times updater loop
        updatePrayerSchedule()
        viewModelScope.launch {
            while (true) {
                delay(60000) // Update countdown every minute
                updatePrayerSchedule()
            }
        }

        // Start compass sensor
        compassSensor.startListening()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
        compassSensor.stopListening()
    }

    fun updatePrayerSchedule() {
        val currentCityName = settings.value.selectedCityName
        val city = PrayerCalculator.supportedCities.find { it.nameBn == currentCityName || it.nameEn.equals(currentCityName, ignoreCase = true) }
            ?: PrayerCalculator.supportedCities.first()
        _prayerSchedule.value = PrayerCalculator.calculatePrayerSchedule(city, Calendar.getInstance())
    }

    fun selectCity(cityName: String) {
        prefsRepo.setSelectedCity(cityName)
        updatePrayerSchedule()
    }

    fun selectSurah(surah: SurahInfo) {
        _selectedSurah.value = surah
        _surahVerses.value = QuranData.getVersesForSurah(surah.number)
        prefsRepo.saveLastRead(surah.number, 1)
    }

    fun selectSurahByNumber(surahNumber: Int) {
        val surah = QuranData.surahList.find { it.number == surahNumber } ?: QuranData.surahList.first()
        selectSurah(surah)
    }

    fun setLanguage(language: AppLanguage) {
        prefsRepo.setLanguage(language)
    }

    fun setThemeMode(mode: ThemeMode) {
        prefsRepo.setThemeMode(mode)
    }

    fun setArabicFontSize(size: Float) {
        prefsRepo.setArabicFontSize(size)
    }

    fun setTranslationFontSize(size: Float) {
        prefsRepo.setTranslationFontSize(size)
    }

    fun toggleHaptic(enabled: Boolean) {
        prefsRepo.setHapticEnabled(enabled)
    }

    fun toggleSound(enabled: Boolean) {
        prefsRepo.setSoundEnabled(enabled)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setHadithCategory(category: HadithCategory?) {
        _selectedHadithCategory.value = category
    }

    fun setAzkarCategory(category: AzkarCategory) {
        _selectedAzkarCategory.value = category
    }

    fun setDuaCategory(category: DuaCategory?) {
        _selectedDuaCategory.value = category
    }

    fun toggleBookmark(type: String, id: String) {
        val key = "$type:$id"
        prefsRepo.toggleBookmark(key)
    }

    fun isBookmarked(type: String, id: String): Boolean {
        return prefsRepo.isBookmarked("$type:$id")
    }

    fun toggleAzkarDone(azkarId: String) {
        prefsRepo.toggleAzkarCompleted(azkarId)
    }

    fun isAzkarDone(azkarId: String): Boolean {
        return settings.value.completedAzkarIds.contains(azkarId)
    }

    fun toggleWorshipGoal(goalId: String) {
        prefsRepo.toggleWorshipGoal(goalId)
    }

    fun isGoalDone(goalId: String): Boolean {
        return settings.value.completedWorshipGoals.contains(goalId)
    }

    // Tasbih Actions
    fun incrementTasbih() {
        val currentCount = settings.value.tasbihCount
        val target = settings.value.tasbihTarget
        val dhikr = settings.value.tasbihDhikrName

        val newCount = currentCount + 1
        prefsRepo.updateTasbih(newCount, target, dhikr)

        if (settings.value.isHapticEnabled) {
            triggerVibration(if (newCount % target == 0) 80L else 25L)
        }
    }

    fun undoTasbih() {
        val currentCount = settings.value.tasbihCount
        if (currentCount > 0) {
            prefsRepo.updateTasbih(currentCount - 1, settings.value.tasbihTarget, settings.value.tasbihDhikrName)
        }
    }

    fun resetTasbih() {
        prefsRepo.updateTasbih(0, settings.value.tasbihTarget, settings.value.tasbihDhikrName)
    }

    fun setTasbihTarget(target: Int) {
        prefsRepo.updateTasbih(settings.value.tasbihCount, target, settings.value.tasbihDhikrName)
    }

    fun setTasbihDhikr(dhikr: String) {
        prefsRepo.updateTasbih(settings.value.tasbihCount, settings.value.tasbihTarget, dhikr)
    }

    private fun triggerVibration(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(durationMs)
                }
            }
        } catch (e: Exception) {
            // Ignore vibration error on emulators
        }
    }

    fun clearAllUserData() {
        prefsRepo.clearAllData()
        updatePrayerSchedule()
    }
}
