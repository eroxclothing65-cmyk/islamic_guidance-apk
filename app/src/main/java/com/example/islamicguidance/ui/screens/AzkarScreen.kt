package com.example.islamicguidance.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
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
import com.example.islamicguidance.data.*
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.IslamicTopBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AzkarAmber
import com.example.ui.theme.EmeraldPrimaryLight

@Composable
fun AzkarScreen(viewModel: IslamicViewModel) {
    val settings by viewModel.settings.collectAsState()
    val language = settings.language
    val selectedCategory by viewModel.selectedAzkarCategory.collectAsState()

    val filteredAzkar = remember(selectedCategory) {
        AzkarData.azkarList.filter { it.category == selectedCategory }
    }

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = AppStrings.get("azkar_title", language),
                subtitle = if (language == AppLanguage.BENGALI) selectedCategory.titleBn else selectedCategory.titleEn
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("azkar_screen")
        ) {
            // Category Chips Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AzkarCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.setAzkarCategory(category) },
                        label = { Text(if (language == AppLanguage.BENGALI) category.titleBn else category.titleEn) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Azkar List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredAzkar, key = { it.id }) { item ->
                    val isCompleted = viewModel.isAzkarDone(item.id)

                    AzkarCardItem(
                        item = item,
                        language = language,
                        isCompleted = isCompleted,
                        onToggleCompleted = { viewModel.toggleAzkarDone(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun AzkarCardItem(
    item: AzkarItem,
    language: AppLanguage,
    isCompleted: Boolean,
    onToggleCompleted: () -> Unit
) {
    var count by remember(item.id) { mutableIntStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("azkar_item_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title & Reference Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) item.titleBn else item.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = item.reference,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // Target badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AccentGold.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "${PrayerCalculator.convertToBengaliNumerals(item.targetCount)} বার" else "${item.targetCount}x",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic text
            Text(
                text = item.arabic,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bengali pronunciation
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "উচ্চারণ: ${item.pronunciationBn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bengali meaning
            Text(
                text = "অর্থ: ${item.bengali}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            if (language == AppLanguage.ENGLISH && item.english.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.english,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Repetition Counter & Completion Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Interactive Tap Button for repetition
                Button(
                    onClick = {
                        if (count < item.targetCount) {
                            count++
                            if (count >= item.targetCount) {
                                onToggleCompleted()
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (count >= item.targetCount || isCompleted) EmeraldPrimaryLight else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("azkar_count_button_${item.id}")
                ) {
                    if (count >= item.targetCount || isCompleted) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সম্পন্ন (${item.targetCount}/${item.targetCount})")
                    } else {
                        Text(
                            text = if (language == AppLanguage.BENGALI) {
                                "গণনা করুন (${PrayerCalculator.convertToBengaliNumerals(count)}/${PrayerCalculator.convertToBengaliNumerals(item.targetCount)})"
                            } else {
                                "Tap to Count ($count/${item.targetCount})"
                            }
                        )
                    }
                }

                // Reset / Completion Checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (count > 0) {
                        IconButton(onClick = { count = 0 }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    IconButton(
                        onClick = onToggleCompleted,
                        modifier = Modifier.testTag("azkar_toggle_done_${item.id}")
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = "Mark done",
                            tint = if (isCompleted) EmeraldPrimaryLight else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
