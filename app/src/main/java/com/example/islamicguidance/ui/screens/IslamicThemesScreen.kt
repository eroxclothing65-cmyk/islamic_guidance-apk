package com.example.islamicguidance.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.AppLanguage
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicThemesScreen(
    viewModel: IslamicViewModel,
    onNavigateBack: () -> Unit
) {
    BackHandler { onNavigateBack() }

    val settings by viewModel.settings.collectAsState()
    val isBengali = settings.language == AppLanguage.BENGALI

    // Currently applied theme
    val activePreset = settings.themePreset
    // Selected theme for preview
    var previewPreset by remember(activePreset) { mutableStateOf(activePreset) }
    var showAppliedMessage by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showAppliedMessage) {
        if (showAppliedMessage) {
            val msg = if (isBengali) "থিম সফলভাবে প্রয়োগ করা হয়েছে!" else "Theme applied successfully!"
            snackbarHostState.showSnackbar(msg)
            showAppliedMessage = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isBengali) "ইসলামিক থিমসমূহ" else "Islamic Themes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isBengali) "৯টি অনন্য নান্দনিক থিম পছন্দ করুন (লিকুইড গ্লাস সহ)" else "Choose from 9 distinct Islamic themes (including Liquid Glass)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("themes_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isBengali) "ফিরে যান" else "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(visible = previewPreset != activePreset) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBengali) "প্রিভিউ করা থিম:" else "Previewing:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isBengali) previewPreset.nameBn else previewPreset.nameEn,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.setThemePreset(previewPreset)
                                showAppliedMessage = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("apply_theme_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "থিম প্রয়োগ করুন" else "Apply Theme",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBengali) "ব্যক্তিগত পছন্দ ও আরামদায়ক পাঠ" else "Personalize Your Experience",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBengali)
                                "দিন, রাত ও আরামদায়ক তিলাওয়াতের জন্য বিশেষ রঙ বিন্যাস। কার্ডে ট্যাপ করে প্রিভিউ দেখুন এবং 'থিম প্রয়োগ করুন' বাটনে চাপ দিন।"
                            else
                                "Specially crafted palettes for day, night, and immersive reading. Tap any card to preview and press 'Apply Theme' to set.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            items(IslamicThemePreset.entries, key = { it.id }) { preset ->
                ThemeCard(
                    preset = preset,
                    isActive = preset == activePreset,
                    isPreviewing = preset == previewPreset,
                    isBengali = isBengali,
                    onSelect = {
                        previewPreset = preset
                    },
                    onApply = {
                        previewPreset = preset
                        viewModel.setThemePreset(preset)
                        showAppliedMessage = true
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeCard(
    preset: IslamicThemePreset,
    isActive: Boolean,
    isPreviewing: Boolean,
    isBengali: Boolean,
    onSelect: () -> Unit,
    onApply: () -> Unit
) {
    // Determine colors for swatch representation
    val (primaryColor, surfaceColor, accentColor, sampleTextColor) = when (preset) {
        IslamicThemePreset.EMERALD_GREEN -> QuadColors(EmeraldPrimaryLight, EmeraldContainerLight, IslamicGold, Color(0xFF1E293B))
        IslamicThemePreset.MIDNIGHT_ISLAMIC -> QuadColors(MidnightPrimary, MidnightSurface, MidnightGold, Color(0xFFE2E8F0))
        IslamicThemePreset.WHITE_AND_GOLD -> QuadColors(WhiteGoldPrimary, WhiteGoldSurface, IslamicGold, Color(0xFF1E293B))
        IslamicThemePreset.MOSQUE_GREEN -> QuadColors(MosqueGreenPrimary, MosqueCreamSurface, MosqueGreenSecondary, Color(0xFF1B382B))
        IslamicThemePreset.ROYAL_ISLAMIC -> QuadColors(RoyalEmerald, RoyalSurface, RoyalImperialGold, Color(0xFFE6EDF3))
        IslamicThemePreset.SAND_AND_OLIVE -> QuadColors(OliveGreenPrimary, SandSurface, DesertSandSecondary, Color(0xFF2C3E2D))
        IslamicThemePreset.MINIMAL_QURAN -> QuadColors(MinimalSlatePrimary, MinimalSurface, MinimalSeaGreenSecondary, Color(0xFF111827))
        IslamicThemePreset.AMOLED_BLACK -> QuadColors(AmoledMintPrimary, AmoledSurface, AmoledGoldSecondary, Color(0xFFF9FAFB))
        IslamicThemePreset.LIQUID_GLASS -> QuadColors(Color(0xFF0D683E), Color(0xEEFFFFFF), Color(0xFFC79218), Color(0xFF0F1E16))
    }

    val cardBorder = when {
        isActive -> BorderStroke(2.dp, IslamicGold)
        isPreviewing -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else -> BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .testTag("theme_card_${preset.id}"),
        shape = RoundedCornerShape(16.dp),
        border = cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isPreviewing || isActive) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Title & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBengali) preset.nameBn else preset.nameEn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isBengali) preset.descriptionBn else preset.descriptionEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isActive) {
                    Surface(
                        color = IslamicGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, IslamicGold)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBengali) "সক্রিয়" else "Active",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = IslamicGold
                            )
                        }
                    }
                } else if (isPreviewing) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isBengali) "প্রিভিউ হচ্ছে" else "Previewing",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mini visual representation of the theme
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = surfaceColor,
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Mini Top bar of preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(primaryColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                color = sampleTextColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(accentColor)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mini Quran Reader mock line
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryColor.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isBengali) "সূরা আল-ফাতিহা • আয়াত ১" else "Surah Al-Fatihah • Verse 1",
                            fontSize = 11.sp,
                            color = sampleTextColor.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "১",
                            fontSize = 10.sp,
                            color = primaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Palette Swatches & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Color swatches
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ColorSwatch(primaryColor, "Primary")
                    ColorSwatch(surfaceColor, "Surface")
                    ColorSwatch(accentColor, "Accent")
                }

                // Action button
                if (!isActive) {
                    FilledTonalButton(
                        onClick = onApply,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("apply_btn_${preset.id}")
                    ) {
                        Text(
                            text = if (isBengali) "প্রয়োগ করুন" else "Apply",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, name: String) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Color.Black.copy(alpha = 0.15f), CircleShape)
    )
}

private data class QuadColors(
    val primary: Color,
    val surface: Color,
    val accent: Color,
    val textColor: Color
)
