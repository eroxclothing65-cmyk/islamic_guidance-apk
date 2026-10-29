package com.example

import com.example.islamicguidance.data.*
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class IslamicGuidanceUnitTest {

    @Test
    fun testSurahListHas114Surahs() {
        assertEquals(114, QuranData.surahList.size)
        assertEquals("الفاتحة", QuranData.surahList.first().nameArabic)
        assertEquals("الناس", QuranData.surahList.last().nameArabic)
    }

    @Test
    fun testSurahAlFatihahHas7Verses() {
        val fatihahVerses = QuranData.getVersesForSurah(1)
        assertEquals(7, fatihahVerses.size)
        assertTrue(fatihahVerses[0].textArabic.contains("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"))
    }

    @Test
    fun testSurahAlBaqarahHas286Verses() {
        val baqarahVerses = QuranData.getVersesForSurah(2)
        assertEquals(286, baqarahVerses.size)
        assertEquals(1, baqarahVerses.first().ayahNumber)
        assertEquals(286, baqarahVerses.last().ayahNumber)
        assertTrue(baqarahVerses[0].textArabic.contains("الم"))
        assertTrue(baqarahVerses[254].textArabic.contains("اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ")) // Ayat al-Kursi (verse 255)
    }

    @Test
    fun testAllShortSurahsHaveCompleteVerses() {
        assertEquals(4, QuranData.getVersesForSurah(112).size) // Al-Ikhlas
        assertEquals(5, QuranData.getVersesForSurah(113).size) // Al-Falaq
        assertEquals(6, QuranData.getVersesForSurah(114).size) // An-Nas
        assertEquals(3, QuranData.getVersesForSurah(108).size) // Al-Kawthar
        assertEquals(3, QuranData.getVersesForSurah(103).size) // Al-Asr
    }

    @Test
    fun testHadithKutubAlSittahCollectionsAndGrading() {
        assertFalse(HadithData.hadiths.isEmpty())
        assertTrue(HadithData.hadiths.any { it.collection.contains("বুখারী") || it.collectionEn.contains("Bukhari") })
        assertTrue(HadithData.hadiths.any { it.collection.contains("মুসলিম") || it.collectionEn.contains("Muslim") })
        assertTrue(HadithData.hadiths.any { it.collection.contains("আবু দাউদ") || it.collectionEn.contains("Abu Dawud") })
        assertTrue(HadithData.hadiths.any { it.collection.contains("তিরমিযী") || it.collectionEn.contains("Tirmidhi") })
        assertTrue(HadithData.hadiths.any { it.collection.contains("নাসায়ী") || it.collectionEn.contains("Nasa'i") })
        assertTrue(HadithData.hadiths.any { it.collection.contains("ইবনে মাজাহ") || it.collectionEn.contains("Ibn Majah") })

        // Check authentication grades
        assertTrue(HadithData.hadiths.any { it.grade.contains("সহীহ") || it.grade.contains("Sahih") })
        assertTrue(HadithData.hadiths.any { it.grade.contains("হাসান") || it.grade.contains("Hasan") })
        assertTrue(HadithData.hadiths.any { it.grade.contains("যয়ীফ") || it.grade.contains("Da'if") })
    }

    @Test
    fun testGardenReachKolkataPrayerCalculation() {
        val gardenReach = PrayerCalculator.supportedCities.first { it.nameEn.contains("Garden Reach") }
        assertEquals(22.5350, gardenReach.latitude, 0.001)
        assertEquals(88.3050, gardenReach.longitude, 0.001)
        assertEquals("Asia/Kolkata", gardenReach.timezoneId)
        assertEquals("ভারত", gardenReach.countryBn)
        assertEquals("West Bengal", gardenReach.state)
        assertEquals("Kolkata", gardenReach.district)

        val schedule = PrayerCalculator.calculatePrayerSchedule(
            city = gardenReach,
            calendar = Calendar.getInstance(),
            method = CalculationMethod.KARACHI,
            asrMethod = AsrJuristicMethod.HANAFI
        )

        assertNotNull(schedule.fajr)
        assertNotNull(schedule.sunrise)
        assertNotNull(schedule.dhuhr)
        assertNotNull(schedule.asr)
        assertNotNull(schedule.maghrib)
        assertNotNull(schedule.isha)
        assertTrue(schedule.city.contains("গার্ডেন রিচ"))
        assertEquals("Kolkata", schedule.district)
        assertEquals("West Bengal", schedule.state)
        assertEquals("ভারত", schedule.country)

        val monthlySchedule = PrayerCalculator.calculateMonthlySchedule(
            city = gardenReach,
            baseCalendar = Calendar.getInstance(),
            method = CalculationMethod.KARACHI,
            asrMethod = AsrJuristicMethod.HANAFI
        )
        assertTrue(monthlySchedule.size >= 28 && monthlySchedule.size <= 31)
    }

    @Test
    fun testLiquidGlassThemePreset() {
        val themes = com.example.ui.theme.IslamicThemePreset.values()
        assertEquals(9, themes.size)
        assertTrue(themes.contains(com.example.ui.theme.IslamicThemePreset.LIQUID_GLASS))
    }

    @Test
    fun testAzkarCategories() {
        assertFalse(AzkarData.azkarList.isEmpty())
        assertTrue(AzkarData.azkarList.any { it.category == AzkarCategory.MORNING })
        assertTrue(AzkarData.azkarList.any { it.category == AzkarCategory.EVENING })
    }

    @Test
    fun testBengaliNumeralsConversion() {
        assertEquals("১১৪", PrayerCalculator.convertToBengaliNumerals(114))
        assertEquals("৩৩", PrayerCalculator.convertToBengaliNumerals(33))
        assertEquals("২৮৬", PrayerCalculator.convertToBengaliNumerals(286))
    }
}

