package com.example.islamicguidance.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.*
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.IslamicTopBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimaryLight
import com.example.ui.theme.HadithBlue

@Composable
fun HadithScreen(viewModel: IslamicViewModel) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val language = settings.language
    val selectedCategory by viewModel.selectedHadithCategory.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredHadiths = remember(searchQuery, selectedCategory) {
        HadithData.hadiths.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.bengali.contains(searchQuery, ignoreCase = true) ||
                    item.english.contains(searchQuery, ignoreCase = true) ||
                    item.arabic.contains(searchQuery) ||
                    item.chapterBn.contains(searchQuery, ignoreCase = true) ||
                    item.number.toString() == searchQuery.trim()

            val matchesCategory = selectedCategory == null || item.category == selectedCategory

            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = AppStrings.get("nav_hadith", language),
                subtitle = "${HadithData.hadiths.size} ${AppStrings.get("hadith_books", language)}"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("hadith_screen")
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(AppStrings.get("search_hadith", language)) },
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
                    .testTag("hadith_search_field"),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            // Category Chips Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.setHadithCategory(null) },
                    label = { Text(if (language == AppLanguage.BENGALI) "সকল হাদিস" else "All Hadith") },
                    shape = RoundedCornerShape(12.dp)
                )

                HadithCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.setHadithCategory(category) },
                        label = { Text(if (language == AppLanguage.BENGALI) category.nameBn else category.nameEn) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Hadith List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredHadiths, key = { it.id }) { hadith ->
                    val isBookmarked = viewModel.isBookmarked("hadith", hadith.id)

                    HadithCardItem(
                        hadith = hadith,
                        language = language,
                        isBookmarked = isBookmarked,
                        onToggleBookmark = { viewModel.toggleBookmark("hadith", hadith.id) },
                        onCopy = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Hadith", "${hadith.arabic}\n\n${hadith.bengali}\n\n[${hadith.collection} : ${hadith.number}]")
                            cm.setPrimaryClip(clip)
                            Toast.makeText(context, "হাদিস কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "${hadith.collection} : ${hadith.number}")
                                putExtra(Intent.EXTRA_TEXT, "${hadith.arabic}\n\n${hadith.bengali}\n\n[${hadith.collection} : ${hadith.number} (${hadith.narrator}) - ${hadith.grade}]\nইসলামিক গাইডেন্স অ্যাপ")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Hadith"))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HadithCardItem(
    hadith: HadithItem,
    language: AppLanguage,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hadith_card_${hadith.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
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
                        text = "${hadith.collection} • ${hadith.number}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = HadithBlue,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldPrimaryLight.copy(alpha = 0.15f),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Text(
                            text = hadith.grade,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldPrimaryLight,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    IconButton(onClick = onToggleBookmark, modifier = Modifier.size(34.dp)) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) AccentGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chapter & Narrator
            Text(
                text = "${hadith.chapterBn} • বর্ণনাকারী: ${hadith.narrator}",
                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic text
            Text(
                text = hadith.arabic,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                ),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))

            // Bengali Translation
            Text(
                text = hadith.bengali,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            if (language == AppLanguage.ENGLISH && hadith.english.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hadith.english,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onCopy,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "কপি", style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onShare,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "শেয়ার", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
