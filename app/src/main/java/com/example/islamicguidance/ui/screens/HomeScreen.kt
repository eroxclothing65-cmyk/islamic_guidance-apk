package com.example.islamicguidance.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.*
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.IslamicBannerCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: IslamicViewModel,
    onNavigateToQuran: () -> Unit,
    onNavigateToSurah: (Int) -> Unit,
    onNavigateToHadith: () -> Unit,
    onNavigateToAzkar: () -> Unit,
    onNavigateToDuas: () -> Unit,
    onNavigateToTasbih: () -> Unit,
    onNavigateToPrayer: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val prayerSchedule by viewModel.prayerSchedule.collectAsState()
    val language = settings.language

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // 1. Header Greeting & Date Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                EmeraldPrimaryLight,
                                Color(0xFF0F4B30)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = AppStrings.get("greeting", language),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = AccentGold,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = AppStrings.get("app_name", language),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                        }

                        // Search icon button
                        IconButton(
                            onClick = onNavigateToSearch,
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                .testTag("home_search_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Date display pills
                    if (prayerSchedule != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NightsStay,
                                        contentDescription = "Hijri",
                                        tint = AccentGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.BENGALI) prayerSchedule!!.hijriDateBn else prayerSchedule!!.hijriDateEn,
                                        style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Gregorian",
                                        tint = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (language == AppLanguage.BENGALI) prayerSchedule!!.gregorianDateBn else prayerSchedule!!.gregorianDateEn,
                                        style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Next Prayer Countdown & Times Carousel
        item {
            prayerSchedule?.let { schedule ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-14).dp)
                        .clickable(onClick = onNavigateToPrayer)
                        .testTag("prayer_countdown_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = "Prayer Time",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = AppStrings.get("next_prayer", language),
                                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                    Text(
                                        text = if (language == AppLanguage.BENGALI) schedule.nextPrayerNameBn else schedule.nextPrayerNameEn,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            // Countdown badge
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                val remText = if (language == AppLanguage.BENGALI) {
                                    "- ${PrayerCalculator.convertToBengaliNumerals(schedule.remainingHours)} ঘণ্টা ${PrayerCalculator.convertToBengaliNumerals(schedule.remainingMinutes)} মি."
                                } else {
                                    "- ${schedule.remainingHours}h ${schedule.remainingMinutes}m"
                                }
                                Text(
                                    text = remText,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick horizontal prayer times
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PrayerTimeChip(AppStrings.get("fajr", language), schedule.fajr, isNext = schedule.nextPrayerNameEn == "Fajr")
                            PrayerTimeChip(AppStrings.get("sunrise", language), schedule.sunrise)
                            PrayerTimeChip(AppStrings.get("dhuhr", language), schedule.dhuhr, isNext = schedule.nextPrayerNameEn == "Dhuhr")
                            PrayerTimeChip(AppStrings.get("asr", language), schedule.asr, isNext = schedule.nextPrayerNameEn == "Asr")
                            PrayerTimeChip(AppStrings.get("maghrib", language), schedule.maghrib, isNext = schedule.nextPrayerNameEn == "Maghrib")
                            PrayerTimeChip(AppStrings.get("isha", language), schedule.isha, isNext = schedule.nextPrayerNameEn == "Isha")
                        }
                    }
                }
            }
        }

        // 3. Quick Access Grid
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = AppStrings.get("quick_access", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = if (language == AppLanguage.BENGALI) "আল-কুরআন" else "Al-Quran",
                        subtitle = if (language == AppLanguage.BENGALI) "১১৪ সূরা" else "114 Surahs",
                        icon = Icons.Default.MenuBook,
                        color = QuranGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToQuran
                    )
                    QuickActionCard(
                        title = if (language == AppLanguage.BENGALI) "হাদিস গ্রন্থ" else "Hadith",
                        subtitle = if (language == AppLanguage.BENGALI) "সহীহ হাদিস" else "Authentic",
                        icon = Icons.Default.LibraryBooks,
                        color = HadithBlue,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToHadith
                    )
                    QuickActionCard(
                        title = if (language == AppLanguage.BENGALI) "দৈনিক আজকার" else "Azkar",
                        subtitle = if (language == AppLanguage.BENGALI) "হিসনুল মুসলিম" else "Hisn Muslim",
                        icon = Icons.Default.Favorite,
                        color = AzkarAmber,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToAzkar
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = if (language == AppLanguage.BENGALI) "মাসনূন দোয়া" else "Duas",
                        subtitle = if (language == AppLanguage.BENGALI) "কুরআন ও সুন্নাহ" else "Supplications",
                        icon = Icons.Default.WbSunny,
                        color = DuaPurple,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToDuas
                    )
                    QuickActionCard(
                        title = if (language == AppLanguage.BENGALI) "ডিজিটাল তাসবিহ" else "Tasbih",
                        subtitle = "${settings.tasbihCount} / ${settings.tasbihTarget}",
                        icon = Icons.Default.FilterVintage,
                        color = EmeraldPrimaryLight,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToTasbih
                    )
                    QuickActionCard(
                        title = if (language == AppLanguage.BENGALI) "কিবলা কম্পাস" else "Qibla",
                        subtitle = "${prayerSchedule?.qiblaBearing?.toInt() ?: 279}° N",
                        icon = Icons.Default.Explore,
                        color = AccentGold,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToQibla
                    )
                }
            }
        }

        // 4. Continue Reading Quran
        item {
            val lastSurah = QuranData.surahList.find { it.number == settings.lastReadSurah } ?: QuranData.surahList.first()
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onNavigateToSurah(lastSurah.number) }
                    .testTag("continue_reading_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(QuranGreen.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${lastSurah.number}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = QuranGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = AppStrings.get("continue_reading", language),
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Text(
                                text = if (language == AppLanguage.BENGALI) lastSurah.nameBengali else lastSurah.nameEnglish,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${lastSurah.nameArabic} • ${lastSurah.totalVerses} ${AppStrings.get("verses_count", language)}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = { onNavigateToSurah(lastSurah.number) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Read",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // 5. Daily Quran Verse Card
        item {
            val verse = QuranData.dailyVerse
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("daily_verse_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = AppStrings.get("daily_verse", language),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.audioPlayer.playAyah(
                                    verse.surahNumber,
                                    verse.ayahNumber,
                                    "সূরা আল-বাকারা",
                                    verse.audioUrl
                                )
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play Ayah Recitation",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Arabic Text
                    Text(
                        text = verse.textArabic,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 36.sp
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Translation
                    Text(
                        text = if (language == AppLanguage.BENGALI) verse.textBengali else verse.textEnglish,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "— সূরা আল-বাকারা (২:২৫৫)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // 6. Daily Hadith Card
        item {
            val hadith = HadithData.dailyHadith
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("daily_hadith_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = HadithBlue.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = AppStrings.get("daily_hadith", language),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = HadithBlue,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = hadith.grade,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldPrimaryLight,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = hadith.arabic,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == AppLanguage.BENGALI) hadith.bengali else hadith.english,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "— ${hadith.collection} : ${hadith.number} (${hadith.narrator})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // 7. Daily Worship Goals Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = AppStrings.get("worship_goals", language),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                viewModel.worshipGoals.forEach { goal ->
                    val isDone = viewModel.isGoalDone(goal.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.toggleWorshipGoal(goal.id) }
                            .testTag("goal_item_${goal.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDone) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) goal.titleBn else goal.titleEn,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isDone) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            )

                            Checkbox(
                                checked = isDone,
                                onCheckedChange = { viewModel.toggleWorshipGoal(goal.id) },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerTimeChip(name: String, time: String, isNext: Boolean = false) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (isNext) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = time,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (isNext) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("quick_action_${title.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
