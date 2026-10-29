package com.example.islamicguidance.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import com.example.islamicguidance.data.ReciterInfo
import com.example.islamicguidance.data.RecitersData
import com.example.islamicguidance.service.PlaybackState
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitersScreen(
    viewModel: IslamicViewModel,
    onNavigateBack: () -> Unit
) {
    BackHandler { onNavigateBack() }

    val settings by viewModel.settings.collectAsState()
    val isBengali = settings.language == AppLanguage.BENGALI

    val playbackState by viewModel.audioPlayer.playbackState.collectAsState()
    val currentTrack by viewModel.audioPlayer.currentTrack.collectAsState()

    val activeReciterId = settings.reciterId
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedSnackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedSnackbarMessage) {
        selectedSnackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            selectedSnackbarMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isBengali) "কুরআন ক্বারী নির্বাচন" else "Quran Reciters",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isBengali) "১০ জন বিশ্বখ্যাত ক্বারীর কণ্ঠ" else "10 World-Renowned Reciters",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.stopAudio()
                            onNavigateBack()
                        },
                        modifier = Modifier.testTag("reciters_back_button")
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp)
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
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBengali) "বিশুদ্ধ কুরআন তিলাওয়াত শুনুন" else "Authentic Quran Recitations",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBengali)
                                "প্রত্যেক ক্বারীর পাশে অডিও প্রিভিউ বাটনে ট্যাপ করে নমুনা তিলাওয়াত শুনুন এবং আপনার পছন্দের ক্বারী নির্বাচন করুন। কুরআন পাঠক স্ক্রিনে স্বয়ংক্রিয়ভাবে নির্বাচিত ক্বারীর কণ্ঠে তিলাওয়াত বাজানো হবে।"
                            else
                                "Tap the audio preview button next to any reciter to hear a sample, then select your preferred reciter. Recitations in the Quran reader will automatically play in their voice.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            items(RecitersData.reciters, key = { it.id }) { reciter ->
                val isSelected = reciter.id == activeReciterId
                val isPlayingThis = (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.BUFFERING) &&
                        currentTrack.reciterId == reciter.id

                ReciterCard(
                    reciter = reciter,
                    isSelected = isSelected,
                    isPlaying = isPlayingThis,
                    playbackState = playbackState,
                    isBengali = isBengali,
                    onSelect = {
                        viewModel.setReciterId(reciter.id)
                        val name = if (isBengali) reciter.nameBn else reciter.nameEn
                        selectedSnackbarMessage = if (isBengali) "$name নির্বাচিত হয়েছে" else "$name selected"
                    },
                    onTogglePreview = {
                        if (isPlayingThis) {
                            viewModel.stopAudio()
                        } else {
                            viewModel.playReciterPreview(reciter.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ReciterCard(
    reciter: ReciterInfo,
    isSelected: Boolean,
    isPlaying: Boolean,
    playbackState: PlaybackState,
    isBengali: Boolean,
    onSelect: () -> Unit,
    onTogglePreview: () -> Unit
) {
    val cardBorder = if (isSelected) {
        BorderStroke(2.dp, IslamicGold)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .testTag("reciter_card_${reciter.id}"),
        shape = RoundedCornerShape(16.dp),
        border = cardBorder,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with reciter initial or mic icon
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.GraphicEq else Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Reciter Names & Style
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBengali) reciter.nameBn else reciter.nameEn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = reciter.nameAr,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Radio Button for selection
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = IslamicGold,
                        unselectedColor = MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier.testTag("reciter_radio_${reciter.id}")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle & Recitation Style Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = reciter.style,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                // Audio preview button
                FilledTonalIconButton(
                    onClick = onTogglePreview,
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("preview_btn_${reciter.id}"),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isPlaying) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isPlaying && playbackState == PlaybackState.BUFFERING) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Stop Preview" else "Play Preview",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlaying) (if (isBengali) "বন্ধ করুন" else "Stop") else (if (isBengali) "নমুনা শুনুন" else "Preview"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isBengali) reciter.descriptionBn else reciter.descriptionEn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                lineHeight = 16.sp
            )
        }
    }
}
