package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class IslamicThemePreset(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val isDarkVariant: Boolean
) {
    EMERALD_GREEN(
        "emerald_green",
        "এমেরাল্ড গ্রিন (Emerald Green)",
        "Emerald Green Islamic",
        "ঐতিহ্যবাহী পান্না সবুজ, মার্জিত শুভ্র ব্যাকগ্রাউন্ড ও সোনালী আভা।",
        "Classic emerald green with clean surfaces and subtle gold highlights.",
        isDarkVariant = false
    ),
    MIDNIGHT_ISLAMIC(
        "midnight_islamic",
        "মিডনাইট ইসলামিক (Midnight)",
        "Midnight Islamic",
        "গাঢ় সবুজ ও চারকোল মিশ্রিত ব্যাকগ্রাউন্ড, চোখের জন্য স্বস্তিকর রাতের পঠন।",
        "Deep green and charcoal background with soft white text for night reading.",
        isDarkVariant = true
    ),
    WHITE_AND_GOLD(
        "white_and_gold",
        "হোয়াইট অ্যান্ড গোল্ড (White & Gold)",
        "White & Gold",
        "পরম শুভ্রতার মাঝে সোনালী রাজকীয় ছোঁয়া ও পরিচ্ছন্ন নকশা।",
        "Pure white canvas with elegant gold ornaments and minimal aesthetic.",
        isDarkVariant = false
    ),
    MOSQUE_GREEN(
        "mosque_green",
        "মসজিদ গ্রিন (Mosque Green)",
        "Mosque Green",
        "মসজিদের গম্বুজ ও মিনার অনুপ্রাণিত ঐতিহ্যবাহী সবুজ ও নরম ক্রিম শেড।",
        "Traditional mosque-inspired green with soft cream surfaces.",
        isDarkVariant = false
    ),
    ROYAL_ISLAMIC(
        "royal_islamic",
        "রয়্যাল ইসলামিক (Royal Islamic)",
        "Royal Islamic",
        "গাঢ় রাজকীয় পান্না ও সমৃদ্ধ স্বর্ণালী আভা সংবলিত প্রিমিয়াম রূপ।",
        "Deep royal emerald and dark green with opulent gold accents.",
        isDarkVariant = true
    ),
    SAND_AND_OLIVE(
        "sand_and_olive",
        "স্যান্ড অ্যান্ড অলিভ (Sand & Olive)",
        "Sand & Olive",
        "মরু বালুকার স্নিগ্ধ আভা ও জয়তুন সবুজের শান্ত সমাহিত আবহ।",
        "Warm desert sand backgrounds with natural soothing olive green.",
        isDarkVariant = false
    ),
    MINIMAL_QURAN(
        "minimal_quran",
        "মিনিমাল কুরআন (Minimal Quran)",
        "Minimal Quran",
        "অনাবিল পাঠ-কেন্দ্রিক ডিজাইন, উচ্চ সুস্পষ্টতা ও বিভ্রান্তিমুক্ত পরিবেশ।",
        "Pure reading-focused interface with optimal Arabic & Bengali clarity.",
        isDarkVariant = false
    ),
    AMOLED_BLACK(
        "amoled_black",
        "অ্যামোলেড ব্ল্যাক (AMOLED Black)",
        "AMOLED Black",
        "শতভাগ বিশুদ্ধ কালো ব্যাকগ্রাউন্ড, উচ্চ বৈসাদৃশ্য ও ব্যাটারি সাশ্রয়ী।",
        "True pitch-black #000000 background for AMOLED screens and battery saving.",
        isDarkVariant = true
    ),
    LIQUID_GLASS(
        "liquid_glass",
        "লিকুইড গ্লাস (Liquid Glass)",
        "Liquid Glass Islamic",
        "স্বচ্ছ ফ্রস্টেড গ্লাস ইফেক্ট, স্নিগ্ধ পান্না ও স্বর্ণালী বর্ডার এবং আধুনিক কাচের নান্দনিকতা।",
        "Translucent frosted glass aesthetics with soft emerald and gold highlights.",
        isDarkVariant = false
    )
}

object IslamicThemePalettes {
    // 1. EMERALD GREEN
    val EmeraldLight = lightColorScheme(
        primary = EmeraldPrimaryLight,
        onPrimary = Color.White,
        primaryContainer = EmeraldContainerLight,
        onPrimaryContainer = Color(0xFF002113),
        secondary = GoldAccentLight,
        onSecondary = Color.White,
        secondaryContainer = GoldContainerLight,
        onSecondaryContainer = Color(0xFF261A00),
        tertiary = Color(0xFF2E7D32),
        background = BackgroundLight,
        onBackground = TextPrimaryLight,
        surface = SurfaceLight,
        onSurface = TextPrimaryLight,
        surfaceVariant = CardBackgroundLight,
        onSurfaceVariant = TextSecondaryLight,
        outline = Color(0xFFD6E2D9)
    )

    // 2. MIDNIGHT ISLAMIC
    val MidnightDark = darkColorScheme(
        primary = MidnightPrimary,
        onPrimary = Color(0xFF003822),
        primaryContainer = MidnightSurfaceVariant,
        onPrimaryContainer = Color(0xFFB4F3CD),
        secondary = MidnightGold,
        onSecondary = Color(0xFF3F3000),
        secondaryContainer = Color(0xFF2E2400),
        onSecondaryContainer = Color(0xFFFFE082),
        tertiary = Color(0xFF81C784),
        background = MidnightBackground,
        onBackground = Color(0xFFF1F5F2),
        surface = MidnightSurface,
        onSurface = Color(0xFFF1F5F2),
        surfaceVariant = MidnightSurfaceVariant,
        onSurfaceVariant = Color(0xFFA5B8AC),
        outline = Color(0xFF2B4435)
    )

    // 3. WHITE & GOLD
    val WhiteGoldLight = lightColorScheme(
        primary = WhiteGoldPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFF6D6),
        onPrimaryContainer = Color(0xFF3B2D00),
        secondary = WhiteGoldSecondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFFECC2),
        onSecondaryContainer = Color(0xFF2A1C00),
        tertiary = Color(0xFFB8860B),
        background = WhiteGoldBackground,
        onBackground = Color(0xFF1F1C16),
        surface = WhiteGoldSurface,
        onSurface = Color(0xFF1F1C16),
        surfaceVariant = WhiteGoldSurfaceVariant,
        onSurfaceVariant = Color(0xFF5A5243),
        outline = Color(0xFFE2DBCB)
    )

    // 4. MOSQUE GREEN
    val MosqueGreenLight = lightColorScheme(
        primary = MosqueGreenPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE2F3E4),
        onPrimaryContainer = Color(0xFF0A2B10),
        secondary = MosqueGreenSecondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFFBF2D5),
        onSecondaryContainer = Color(0xFF332600),
        tertiary = Color(0xFF388E3C),
        background = MosqueCreamBackground,
        onBackground = Color(0xFF1E2B21),
        surface = MosqueCreamSurface,
        onSurface = Color(0xFF1E2B21),
        surfaceVariant = MosqueCreamVariant,
        onSurfaceVariant = Color(0xFF4C5D50),
        outline = Color(0xFFD3DDD5)
    )

    // 5. ROYAL ISLAMIC
    val RoyalDark = darkColorScheme(
        primary = RoyalEmerald,
        onPrimary = Color(0xFF003822),
        primaryContainer = RoyalSurfaceVariant,
        onPrimaryContainer = Color(0xFFB4F3CD),
        secondary = RoyalImperialGold,
        onSecondary = Color(0xFF3F3000),
        secondaryContainer = Color(0xFF382C00),
        onSecondaryContainer = Color(0xFFFFEE8C),
        tertiary = Color(0xFF00E676),
        background = RoyalBackground,
        onBackground = Color(0xFFFAFDFB),
        surface = RoyalSurface,
        onSurface = Color(0xFFFAFDFB),
        surfaceVariant = RoyalSurfaceVariant,
        onSurfaceVariant = Color(0xFFBAD6C7),
        outline = Color(0xFF1F4A34)
    )

    // 6. SAND & OLIVE
    val SandOliveLight = lightColorScheme(
        primary = OliveGreenPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE7ECCF),
        onPrimaryContainer = Color(0xFF1E280C),
        secondary = DesertSandSecondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFF6ECE0),
        onSecondaryContainer = Color(0xFF352614),
        tertiary = Color(0xFF6B8E23),
        background = SandBackground,
        onBackground = Color(0xFF28261E),
        surface = SandSurface,
        onSurface = Color(0xFF28261E),
        surfaceVariant = SandSurfaceVariant,
        onSurfaceVariant = Color(0xFF5A574C),
        outline = Color(0xFFDCD6C7)
    )

    // 7. MINIMAL QURAN
    val MinimalLight = lightColorScheme(
        primary = MinimalSlatePrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFE5ECF2),
        onPrimaryContainer = Color(0xFF121B24),
        secondary = MinimalSeaGreenSecondary,
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE0F4F0),
        onSecondaryContainer = Color(0xFF0A312A),
        tertiary = Color(0xFF34495E),
        background = MinimalPaperBackground,
        onBackground = Color(0xFF1A1D20),
        surface = MinimalSurface,
        onSurface = Color(0xFF1A1D20),
        surfaceVariant = MinimalSurfaceVariant,
        onSurfaceVariant = Color(0xFF4C535A),
        outline = Color(0xFFD6DBE0)
    )

    // 8. AMOLED BLACK
    val AmoledDark = darkColorScheme(
        primary = AmoledMintPrimary,
        onPrimary = Color(0xFF003816),
        primaryContainer = Color(0xFF141414),
        onPrimaryContainer = Color(0xFF80FFAE),
        secondary = AmoledGoldSecondary,
        onSecondary = Color(0xFF3F3000),
        secondaryContainer = Color(0xFF1A1708),
        onSecondaryContainer = Color(0xFFFFEC99),
        tertiary = Color(0xFF69F0AE),
        background = AmoledBackground,
        onBackground = Color(0xFFFFFFFF),
        surface = AmoledSurface,
        onSurface = Color(0xFFFFFFFF),
        surfaceVariant = AmoledSurfaceVariant,
        onSurfaceVariant = Color(0xFFBDBDBD),
        outline = Color(0xFF2D2D2D)
    )

    // 9. LIQUID GLASS LIGHT
    val LiquidGlassLight = lightColorScheme(
        primary = Color(0xFF0D683E),
        onPrimary = Color.White,
        primaryContainer = Color(0x330D683E),
        onPrimaryContainer = Color(0xFF002A16),
        secondary = Color(0xFFC79218),
        onSecondary = Color.White,
        secondaryContainer = Color(0x28C79218),
        onSecondaryContainer = Color(0xFF382600),
        tertiary = Color(0xFF00897B),
        background = Color(0xFFF0F5F2),
        onBackground = Color(0xFF0F1E16),
        surface = Color(0xEEFFFFFF),
        onSurface = Color(0xFF0F1E16),
        surfaceVariant = Color(0xD8F8FCFA),
        onSurfaceVariant = Color(0xFF32483E),
        outline = Color(0x400D683E)
    )

    // 9. LIQUID GLASS DARK
    val LiquidGlassDark = darkColorScheme(
        primary = Color(0xFF38D38E),
        onPrimary = Color(0xFF003820),
        primaryContainer = Color(0x3338D38E),
        onPrimaryContainer = Color(0xFF9CF7C8),
        secondary = Color(0xFFF7CA45),
        onSecondary = Color(0xFF3E2E00),
        secondaryContainer = Color(0x28F7CA45),
        onSecondaryContainer = Color(0xFFFFE082),
        tertiary = Color(0xFF4DB6AC),
        background = Color(0xFF08120D),
        onBackground = Color(0xFFF0F5F2),
        surface = Color(0xCC112018),
        onSurface = Color(0xFFF0F5F2),
        surfaceVariant = Color(0xB2182D23),
        onSurfaceVariant = Color(0xFFA6BEB2),
        outline = Color(0x4D38D38E)
    )

    fun getColorScheme(preset: IslamicThemePreset, isDarkSystem: Boolean): ColorScheme {
        return when (preset) {
            IslamicThemePreset.EMERALD_GREEN -> if (isDarkSystem) MidnightDark else EmeraldLight
            IslamicThemePreset.MIDNIGHT_ISLAMIC -> MidnightDark
            IslamicThemePreset.WHITE_AND_GOLD -> WhiteGoldLight
            IslamicThemePreset.MOSQUE_GREEN -> MosqueGreenLight
            IslamicThemePreset.ROYAL_ISLAMIC -> RoyalDark
            IslamicThemePreset.SAND_AND_OLIVE -> SandOliveLight
            IslamicThemePreset.MINIMAL_QURAN -> MinimalLight
            IslamicThemePreset.AMOLED_BLACK -> AmoledDark
            IslamicThemePreset.LIQUID_GLASS -> if (isDarkSystem) LiquidGlassDark else LiquidGlassLight
        }
    }
}

@Composable
fun IslamicGuidanceTheme(
    themePreset: IslamicThemePreset = IslamicThemePreset.EMERALD_GREEN,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val effectiveColorScheme = IslamicThemePalettes.getColorScheme(themePreset, darkTheme)

    MaterialTheme(
        colorScheme = effectiveColorScheme,
        typography = Typography,
        content = content
    )
}
