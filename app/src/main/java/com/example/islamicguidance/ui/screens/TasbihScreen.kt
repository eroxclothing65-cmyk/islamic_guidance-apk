package com.example.islamicguidance.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.AppLanguage
import com.example.islamicguidance.data.AppStrings
import com.example.islamicguidance.data.PrayerCalculator
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.IslamicTopBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimaryLight

@Composable
fun TasbihScreen(
    viewModel: IslamicViewModel,
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {}
) {
    val settings by viewModel.settings.collectAsState()
    val language = settings.language

    var showResetDialog by remember { mutableStateOf(false) }

    val dhikrPresets = listOf(
        "سُبْحَانَ اللَّهِ" to "সুবহানাল্লাহ (আল্লাহ পরম পবিত্র)",
        "الْحَمْدُ لِلَّهِ" to "আলহামদুলিল্লাহ (সমস্ত প্রশংসা আল্লাহর)",
        "اللَّهُ أَكْبَرُ" to "আল্লাহু আকবার (আল্লাহ সর্বশ্রেষ্ঠ)",
        "لَا إِلٰهَ إِلَّا اللّٰهُ" to "লা ইলাহা ইল্লাল্লাহ (আল্লাহ ছাড়া কোন উপাস্য নেই)",
        "أَسْتَغْفِرُ اللَّهَ" to "আস্তাগফিরুল্লাহ (আমি আল্লাহর ক্ষমা চাই)",
        "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ" to "সুবহানাল্লাহি ওয়া বিহামদিহি",
        "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ" to "সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম"
    )

    val targetPresets = listOf(33, 100, 1000)

    val progress = remember(settings.tasbihCount, settings.tasbihTarget) {
        if (settings.tasbihTarget > 0) {
            (settings.tasbihCount.toFloat() / settings.tasbihTarget.toFloat()).coerceIn(0f, 1f)
        } else 0f
    }
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "tasbih_progress")

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(AppStrings.get("reset_confirm_title", language)) },
            text = { Text(AppStrings.get("reset_confirm_msg", language)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetTasbih()
                        showResetDialog = false
                    },
                    modifier = Modifier.testTag("confirm_reset_tasbih")
                ) {
                    Text(AppStrings.get("yes_reset", language), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(AppStrings.get("cancel", language))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = AppStrings.get("tasbih_title", language),
                subtitle = "ডিজিটাল জিকির ও তাসবিহ",
                canNavigateBack = canNavigateBack,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .testTag("tasbih_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Dhikr Presets Carousel
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "জিকির নির্বাচন করুন:",
                    style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dhikrPresets.forEach { (arabic, bangla) ->
                        val isSelected = settings.tasbihDhikrName == bangla
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setTasbihDhikr(bangla) },
                            label = { Text(arabic, fontWeight = FontWeight.Bold) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Current Dhikr Bangla Display
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = settings.tasbihDhikrName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // 2. Large Circular Tap Counter
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                EmeraldPrimaryLight,
                                Color(0xFF06331F)
                            )
                        )
                    )
                    .clickable(onClick = { viewModel.incrementTasbih() })
                    .testTag("tasbih_tap_button"),
                contentAlignment = Alignment.Center
            ) {
                // Circular Progress Ring Indicator
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(232.dp),
                    strokeWidth = 6.dp,
                    color = AccentGold,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) PrayerCalculator.convertToBengaliNumerals(settings.tasbihCount) else "${settings.tasbihCount}",
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 62.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "লক্ষ্য: ${if (language == AppLanguage.BENGALI) PrayerCalculator.convertToBengaliNumerals(settings.tasbihTarget) else settings.tasbihTarget}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = AppStrings.get("tap_to_count", language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // 3. Targets and Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Target Presets
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    targetPresets.forEach { target ->
                        val isSelected = settings.tasbihTarget == target
                        OutlinedButton(
                            onClick = { viewModel.setTasbihTarget(target) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                            ),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "${PrayerCalculator.convertToBengaliNumerals(target)}" else "$target",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Controls: Undo, Reset, Haptic
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = { viewModel.undoTasbih() },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("tasbih_undo_button")
                    ) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.get("undo", language))
                    }

                    FilledTonalButton(
                        onClick = { showResetDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier.testTag("tasbih_reset_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.get("reset", language))
                    }

                    IconButton(
                        onClick = { viewModel.toggleHaptic(!settings.isHapticEnabled) },
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (settings.isHapticEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = "Haptic feedback",
                            tint = if (settings.isHapticEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
