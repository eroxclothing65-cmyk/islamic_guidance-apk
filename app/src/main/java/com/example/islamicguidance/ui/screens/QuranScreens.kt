package com.example.islamicguidance.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.*
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.IslamicTopBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimaryLight
import com.example.ui.theme.QuranGreen

@Composable
fun QuranListScreen(
    viewModel: IslamicViewModel,
    onSurahSelected: (SurahInfo) -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val language = settings.language
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") } // "all", "makki", "madani"

    val filteredSurahs = remember(searchQuery, selectedFilter) {
        QuranData.surahList.filter { surah ->
            val matchesSearch = searchQuery.isBlank() ||
                    surah.nameBengali.contains(searchQuery, ignoreCase = true) ||
                    surah.nameEnglish.contains(searchQuery, ignoreCase = true) ||
                    surah.nameArabic.contains(searchQuery) ||
                    surah.number.toString() == searchQuery.trim()

            val matchesFilter = when (selectedFilter) {
                "makki" -> surah.revelationType.equals("Makki", ignoreCase = true)
                "madani" -> surah.revelationType.equals("Madani", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = AppStrings.get("nav_quran", language),
                subtitle = "${QuranData.surahList.size} ${AppStrings.get("surah_list", language)}"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("quran_list_screen")
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(AppStrings.get("search_surah", language)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("quran_search_field"),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "all",
                    onClick = { selectedFilter = "all" },
                    label = { Text(if (language == AppLanguage.BENGALI) "সকল সূরা (১১৪)" else "All (114)") },
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = selectedFilter == "makki",
                    onClick = { selectedFilter = "makki" },
                    label = { Text(if (language == AppLanguage.BENGALI) "মাক্কী সূরা" else "Makki") },
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = selectedFilter == "madani",
                    onClick = { selectedFilter = "madani" },
                    label = { Text(if (language == AppLanguage.BENGALI) "মাদানী সূরা" else "Madani") },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Surahs List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredSurahs, key = { it.number }) { surah ->
                    SurahListItem(
                        surah = surah,
                        language = language,
                        onClick = {
                            viewModel.selectSurah(surah)
                            onSurahSelected(surah)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: SurahInfo,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("surah_item_${surah.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Surah Number
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = if (language == AppLanguage.BENGALI) surah.nameBengali else surah.nameEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${surah.englishMeaning} • ${surah.totalVerses} ${AppStrings.get("verses_count", language)}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (language == AppLanguage.BENGALI) surah.revelationTypeBn else surah.revelationType,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // Arabic Surah Name Calligraphy
            Text(
                text = surah.nameArabic,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun SurahDetailScreen(
    viewModel: IslamicViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val language = settings.language
    val selectedSurah by viewModel.selectedSurah.collectAsState()
    val verses by viewModel.surahVerses.collectAsState()

    var showFontControls by remember { mutableStateOf(false) }

    if (selectedSurah == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val surah = selectedSurah!!

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = if (language == AppLanguage.BENGALI) surah.nameBengali else surah.nameEnglish,
                subtitle = "${surah.nameArabic} • ${surah.totalVerses} ${AppStrings.get("verses_count", language)}",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { showFontControls = !showFontControls },
                        modifier = Modifier.testTag("toggle_font_controls")
                    ) {
                        Icon(Icons.Default.FormatSize, contentDescription = "Font Size")
                    }
                    IconButton(
                        onClick = {
                            val firstAyah = verses.firstOrNull()
                            if (firstAyah != null) {
                                viewModel.audioPlayer.playAyah(
                                    surah.number,
                                    firstAyah.ayahNumber,
                                    surah.nameBengali,
                                    firstAyah.audioUrl
                                )
                            }
                        },
                        modifier = Modifier.testTag("play_surah_audio")
                    ) {
                        Icon(Icons.Default.PlayCircle, contentDescription = "Play Recitation")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("surah_detail_screen")
        ) {
            // Font Size Adjusters Panel
            AnimatedVisibility(visible = showFontControls) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${AppStrings.get("arabic_font_size", language)}: ${settings.arabicFontSize.toInt()}sp",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Slider(
                            value = settings.arabicFontSize,
                            onValueChange = { viewModel.setArabicFontSize(it) },
                            valueRange = 22f..40f,
                            steps = 9
                        )

                        Text(
                            text = "${AppStrings.get("translation_font_size", language)}: ${settings.translationFontSize.toInt()}sp",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Slider(
                            value = settings.translationFontSize,
                            onValueChange = { viewModel.setTranslationFontSize(it) },
                            valueRange = 14f..26f,
                            steps = 6
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Surah Header Banner with Bismillah
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = surah.nameArabic,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${surah.nameBengali} (${surah.englishMeaning})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${if (language == AppLanguage.BENGALI) surah.revelationTypeBn else surah.revelationType} • ${surah.totalVerses} ${AppStrings.get("verses_count", language)}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )

                            if (surah.number != 9) { // At-Tawbah does not have Bismillah
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Verses
                items(verses, key = { "${it.surahNumber}_${it.ayahNumber}" }) { ayah ->
                    AyahCard(
                        ayah = ayah,
                        arabicFontSize = settings.arabicFontSize,
                        translationFontSize = settings.translationFontSize,
                        language = language,
                        isBookmarked = viewModel.isBookmarked("ayah", "${ayah.surahNumber}_${ayah.ayahNumber}"),
                        onToggleBookmark = { viewModel.toggleBookmark("ayah", "${ayah.surahNumber}_${ayah.ayahNumber}") },
                        onPlayAudio = {
                            viewModel.audioPlayer.playAyah(
                                ayah.surahNumber,
                                ayah.ayahNumber,
                                surah.nameBengali,
                                ayah.audioUrl
                            )
                        },
                        onCopy = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Quran Ayah", "${ayah.textArabic}\n\n${ayah.textBengali}\n\n[সূরা ${surah.nameBengali} : ${ayah.ayahNumber}]")
                            cm.setPrimaryClip(clip)
                            Toast.makeText(context, AppStrings.get("verse_copied", language), Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "সূরা ${surah.nameBengali} আয়াত ${ayah.ayahNumber}")
                                putExtra(Intent.EXTRA_TEXT, "${ayah.textArabic}\n\n${ayah.textBengali}\n\n[কুরআন মাজীদ: সূরা ${surah.nameBengali} (${ayah.surahNumber}:${ayah.ayahNumber})]\nইসলামিক গাইডেন্স অ্যাপ")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Verse"))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun AyahCard(
    ayah: Ayah,
    arabicFontSize: Float,
    translationFontSize: Float,
    language: AppLanguage,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onPlayAudio: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ayah_card_${ayah.ayahNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Verse Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ayah badge
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${ayah.ayahNumber}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Action icons
                Row {
                    IconButton(onClick = onPlayAudio, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Play Recitation",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(onClick = onToggleBookmark, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) AccentGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(onClick = onCopy, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onShare, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quranic Arabic Text
            Text(
                text = ayah.textArabic,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = arabicFontSize.sp,
                    lineHeight = (arabicFontSize * 1.6f).sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            // Bengali Translation
            Text(
                text = ayah.textBengali,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = translationFontSize.sp,
                    lineHeight = (translationFontSize * 1.5f).sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            // English Translation if language is set to English
            if (language == AppLanguage.ENGLISH && ayah.textEnglish.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.textEnglish,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
