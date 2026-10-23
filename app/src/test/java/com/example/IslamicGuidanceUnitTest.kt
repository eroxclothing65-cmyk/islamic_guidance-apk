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
    fun testHadithCollections() {
        assertFalse(HadithData.hadiths.isEmpty())
        assertTrue(HadithData.hadiths.any { it.collection.contains("বুখারী") })
        assertTrue(HadithData.hadiths.any { it.collection.contains("মুসলিম") })
    }

    @Test
    fun testAzkarCategories() {
        assertFalse(AzkarData.azkarList.isEmpty())
        assertTrue(AzkarData.azkarList.any { it.category == AzkarCategory.MORNING })
        assertTrue(AzkarData.azkarList.any { it.category == AzkarCategory.EVENING })
    }

    @Test
    fun testPrayerCalculation() {
        val dhaka = PrayerCalculator.supportedCities.first { it.nameEn == "Dhaka" }
        val schedule = PrayerCalculator.calculatePrayerSchedule(dhaka, Calendar.getInstance())

        assertNotNull(schedule.fajr)
        assertNotNull(schedule.dhuhr)
        assertNotNull(schedule.asr)
        assertNotNull(schedule.maghrib)
        assertNotNull(schedule.isha)
        assertTrue(schedule.qiblaBearing > 270f && schedule.qiblaBearing < 290f)
    }

    @Test
    fun testBengaliNumeralsConversion() {
        assertEquals("১১৪", PrayerCalculator.convertToBengaliNumerals(114))
        assertEquals("৩৩", PrayerCalculator.convertToBengaliNumerals(33))
    }
}
