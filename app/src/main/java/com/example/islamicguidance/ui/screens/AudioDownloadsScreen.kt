package com.example.islamicguidance.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.AppLanguage
import com.example.islamicguidance.data.QuranData
import com.example.islamicguidance.data.RecitersData
import com.example.islamicguidance.data.SurahInfo
import com.example.islamicguidance.service.DownloadProgress
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioDownloadsScreen(
    viewModel: IslamicViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReciters: () -> Unit
) {
    BackHandler { onNavigateBack() }

    val settings by viewModel.settings.collectAsState()
    val isBengali = settings.language == AppLanguage.BENGALI

    val downloadState: Map<String, DownloadProgress> by viewModel.downloadManager.downloadState.collectAsState()
    val totalStorageBytes: Long by viewModel.downloadManager.totalStorageBytes.collectAsState()
    val formattedStorage = viewModel.downloadManager.getFormattedStorageUsage(totalStorageBytes)

    val currentReciter = RecitersData.getReciter(settings.reciterId)
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.downloadManager.updateStorageUsage()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isBengali) "অডিও ও অফলাইন ডাউনলোড" else "Audio & Offline Downloads",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isBengali) "ইন্টারনেট ছাড়াই কুরআন শুনুন" else "Listen to Quran without internet",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("downloads_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isBengali) "ফিরে যান" else "Back"
                        )
                    }
                },
                actions = {
                    if (totalStorageBytes > 0L) {
                        IconButton(
                            onClick = { showDeleteAllDialog = true },
                            modifier = Modifier.testTag("delete_all_downloads_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = if (isBengali) "সকল অডিও মুছুন" else "Delete all audio",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp)
        ) {
            // Storage card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SdStorage,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBengali) "অফলাইন স্টোরেজ ব্যবহার" else "Offline Storage Used",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Text(
                                text = formattedStorage,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = IslamicGold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Current active reciter info row
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isBengali) "ডাউনলোডের ক্বারী:" else "Download Reciter:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (isBengali) currentReciter.nameBn else currentReciter.nameEn,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                TextButton(
                                    onClick = onNavigateToReciters,
                                    modifier = Modifier.testTag("change_reciter_button")
                                ) {
                                    Text(
                                        text = if (isBengali) "পরিবর্তন" else "Change",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // List of all Surahs with status
            items(QuranData.surahList, key = { it.number }) { surah ->
                val downloadKey = "${currentReciter.id}:${surah.number}"
                val progress = downloadState[downloadKey]
                val isDownloaded = remember(totalStorageBytes, progress) {
                    viewModel.downloadManager.isSurahDownloaded(currentReciter.id, surah.number, surah.totalVerses)
                }

                SurahDownloadItemCard(
                    surah = surah,
                    isDownloaded = isDownloaded,
                    progress = progress,
                    isBengali = isBengali,
                    onStartDownload = {
                        viewModel.downloadManager.downloadSurah(surah.number, surah.totalVerses, currentReciter.id)
                    },
                    onCancelDownload = {
                        viewModel.downloadManager.cancelDownload(currentReciter.id, surah.number)
                    },
                    onDeleteDownload = {
                        viewModel.downloadManager.deleteDownloadedSurah(currentReciter.id, surah.number, surah.totalVerses)
                    }
                )
            }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = {
                Text(text = if (isBengali) "সকল অডিও মুছবেন?" else "Delete All Audio?")
            },
            text = {
                Text(
                    text = if (isBengali)
                        "আপনার ডিভাইসে সেভ করা সকল অফলাইন তিলাওয়াত ফাইল মুছে ফেলা হবে।"
                    else
                        "All offline recitation audio files saved on your device will be removed."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.downloadManager.deleteAllDownloads()
                        showDeleteAllDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = if (isBengali) "মুছে ফেলুন" else "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text(text = if (isBengali) "বাতিল" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun SurahDownloadItemCard(
    surah: SurahInfo,
    isDownloaded: Boolean,
    progress: com.example.islamicguidance.service.DownloadProgress?,
    isBengali: Boolean,
    onStartDownload: () -> Unit,
    onCancelDownload: () -> Unit,
    onDeleteDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .testTag("surah_download_card_${surah.number}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isDownloaded) IslamicGold.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Surah Number badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDownloaded) IslamicGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        fontWeight = FontWeight.Bold,
                        color = if (isDownloaded) IslamicGold else MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Names
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBengali) surah.nameBengali else surah.nameEnglish,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${surah.nameArabic} • ${surah.totalVerses} ${if (isBengali) "আয়াত" else "Verses"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Status Action
                when {
                    progress?.isDownloading == true -> {
                        IconButton(
                            onClick = onCancelDownload,
                            modifier = Modifier.testTag("cancel_download_${surah.number}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = if (isBengali) "বাতিল" else "Cancel",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    isDownloaded -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = onDeleteDownload,
                                modifier = Modifier.testTag("delete_download_${surah.number}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = if (isBengali) "মুছুন" else "Delete",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    else -> {
                        IconButton(
                            onClick = onStartDownload,
                            modifier = Modifier.testTag("start_download_${surah.number}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = if (isBengali) "ডাউনলোড" else "Download",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Downloading Progress Bar
            if (progress?.isDownloading == true) {
                Spacer(modifier = Modifier.height(10.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isBengali)
                                "ডাউনলোড হচ্ছে: আয়াত ${progress.currentAyah}/${progress.totalAyahs}"
                            else
                                "Downloading: Verse ${progress.currentAyah}/${progress.totalAyahs}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "${(progress.progressPercent * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress.progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            } else if (isDownloaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isBengali) "✓ অফলাইনে উপলব্ধ" else "✓ Offline Available",
                    style = MaterialTheme.typography.labelSmall,
                    color = IslamicGold
                )
            }
        }
    }
}
