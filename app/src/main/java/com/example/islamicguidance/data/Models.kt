package com.example.islamicguidance.data

data class SurahInfo(
    val number: Int,
    val nameArabic: String,
    val nameBengali: String,
    val nameEnglish: String,
    val englishMeaning: String,
    val totalVerses: Int,
    val revelationType: String, // "Makki" or "Madani"
    val revelationTypeBn: String, // "মাক্কী" or "মাদানী"
    val startPage: Int = 1
)

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val textBengali: String,
    val textEnglish: String,
    val audioUrl: String = ""
)

enum class HadithCategory(val key: String, val nameBn: String, val nameEn: String) {
    FAITH("faith", "ঈমান ও বিশ্বাস", "Faith & Belief"),
    PRAYER("prayer", "নামাজ ও পবিত্রতা", "Prayer & Purification"),
    CHARITY("charity", "দান-সদকা ও যাকাত", "Charity & Zakat"),
    FASTING("fasting", "রোজা ও রমজান", "Fasting & Ramadan"),
    CHARACTER("character", "সদাচরণ ও উত্তম চরিত্র", "Good Character & Morals"),
    FAMILY("family", "পরিবার ও আত্মীয়তা", "Family & Kinship"),
    KNOWLEDGE("knowledge", "জ্ঞান ও ইলম", "Knowledge & Wisdom"),
    DAILY("daily", "দৈনন্দিন জীবন ও আদব", "Daily Etiquettes")
}

data class HadithItem(
    val id: String,
    val collection: String, // e.g. "সহীহ বুখারী" / "Sahih al-Bukhari"
    val collectionEn: String,
    val number: Int,
    val chapter: String,
    val chapterBn: String,
    val arabic: String,
    val bengali: String,
    val english: String,
    val grade: String, // "সহীহ (Sahih)"
    val narrator: String,
    val category: HadithCategory
)

enum class AzkarCategory(val key: String, val titleBn: String, val titleEn: String) {
    MORNING("morning", "সকালের আজকার", "Morning Azkar"),
    EVENING("evening", "সন্ধ্যার আজকার", "Evening Azkar"),
    AFTER_PRAYER("after_prayer", "নামাজের পরের আজকার", "After Prayer Azkar"),
    SLEEPING("sleeping", "ঘুমানোর আগের আমল", "Before Sleeping"),
    WAKING("waking", "ঘুম থেকে ওঠার পর", "After Waking Up"),
    HOME("home", "ঘরে প্রবেশ ও প্রস্থান", "Home Entry & Exit"),
    MOSQUE("mosque", "মসজিদে প্রবেশ ও প্রস্থান", "Mosque Entry & Exit"),
    ISTIGHFAR("istighfar", "ইস্তিগফার ও তওবা", "Istighfar & Repentance"),
    SALAWAT("salawat", "দরূদ ও সালাম", "Salawat on Prophet")
}

data class AzkarItem(
    val id: String,
    val category: AzkarCategory,
    val title: String,
    val titleBn: String,
    val arabic: String,
    val pronunciationBn: String,
    val bengali: String,
    val english: String,
    val targetCount: Int,
    val reference: String,
    val virtue: String = ""
)

enum class DuaCategory(val key: String, val titleBn: String, val titleEn: String) {
    RABBANA("rabbana", "কুরআনের রব্বানা দোয়া", "Quranic Rabbana Duas"),
    PROTECTION("protection", "বিপদ ও অনিষ্ট থেকে রক্ষা", "Protection & Safety"),
    FORGIVENESS("forgiveness", "ক্ষমা ও তওবার দোয়া", "Forgiveness & Mercy"),
    PARENTS("parents", "বাবা-মা ও পরিবারের জন্য", "Parents & Family"),
    ANXIETY("anxiety", "উদ্বেগ ও দুশ্চিন্তা মুক্তি", "Relief from Distress"),
    HEALTH("health", "সুস্থতা ও রোগমুক্তি", "Health & Healing"),
    KNOWLEDGE("knowledge", "ইলম ও হিদায়াত বৃদ্ধি", "Knowledge & Guidance")
}

data class DuaItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val arabic: String,
    val pronunciationBn: String,
    val bengali: String,
    val english: String,
    val reference: String,
    val isQuranic: Boolean,
    val category: DuaCategory
)

data class PrayerSchedule(
    val city: String,
    val country: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val nextPrayerNameBn: String,
    val nextPrayerNameEn: String,
    val remainingHours: Int,
    val remainingMinutes: Int,
    val hijriDateBn: String,
    val hijriDateEn: String,
    val gregorianDateBn: String,
    val gregorianDateEn: String,
    val qiblaBearing: Float
)

data class BookmarkItem(
    val id: String,
    val type: String, // "quran", "hadith", "dua"
    val title: String,
    val subtitle: String,
    val targetId: String,
    val targetExtra: String = ""
)

data class WorshipGoalItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val isCompleted: Boolean = false
)
