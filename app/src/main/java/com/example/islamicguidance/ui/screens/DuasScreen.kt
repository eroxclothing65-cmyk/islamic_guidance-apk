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
import com.example.ui.theme.DuaPurple
import com.example.ui.theme.EmeraldPrimaryLight

@Composable
fun DuasScreen(
    viewModel: IslamicViewModel,
    canNavigateBack: Boolean = true,
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val language = settings.language
    val selectedCategory by viewModel.selectedDuaCategory.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredDuas = remember(searchQuery, selectedCategory) {
        DuaData.duasList.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.bengali.contains(searchQuery, ignoreCase = true) ||
                    item.english.contains(searchQuery, ignoreCase = true) ||
                    item.titleBn.contains(searchQuery, ignoreCase = true) ||
                    item.arabic.contains(searchQuery)

            val matchesCategory = selectedCategory == null || item.category == selectedCategory

            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = AppStrings.get("duas_title", language),
                subtitle = "কুরআন ও সুন্নাহর মাসনূন দোয়াসমূহ",
                canNavigateBack = canNavigateBack,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("duas_screen")
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(AppStrings.get("search_dua", language)) },
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
                    .testTag("dua_search_field"),
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
                    onClick = { viewModel.setDuaCategory(null) },
                    label = { Text(if (language == AppLanguage.BENGALI) "সকল দোয়া" else "All Duas") },
                    shape = RoundedCornerShape(12.dp)
                )

                DuaCategory.entries.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { viewModel.setDuaCategory(category) },
                        label = { Text(if (language == AppLanguage.BENGALI) category.titleBn else category.titleEn) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Duas List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredDuas, key = { it.id }) { item ->
                    val isBookmarked = viewModel.isBookmarked("dua", item.id)

                    DuaCardItem(
                        item = item,
                        language = language,
                        isBookmarked = isBookmarked,
                        onToggleBookmark = { viewModel.toggleBookmark("dua", item.id) },
                        onCopy = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Dua", "${item.arabic}\n\nউচ্চারণ: ${item.pronunciationBn}\n\nঅর্থ: ${item.bengali}\n\n[সূত্র: ${item.reference}]")
                            cm.setPrimaryClip(clip)
                            Toast.makeText(context, "দোয়া কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, item.titleBn)
                                putExtra(Intent.EXTRA_TEXT, "${item.titleBn}\n\n${item.arabic}\n\nউচ্চারণ: ${item.pronunciationBn}\n\nঅর্থ: ${item.bengali}\n\n[তথ্যসূত্র: ${item.reference}]\nইসলামিক গাইডেন্স অ্যাপ")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Dua"))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DuaCardItem(
    item: DuaItem,
    language: AppLanguage,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dua_item_${item.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) item.titleBn else item.titleEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = item.reference,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AccentGold,
                            fontWeight = FontWeight.SemiBold
                        )
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

            // Pronunciation
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "উচ্চারণ: ${item.pronunciationBn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
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
