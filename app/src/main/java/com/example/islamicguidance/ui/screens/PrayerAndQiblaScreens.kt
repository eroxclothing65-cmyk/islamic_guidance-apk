package com.example.islamicguidance.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicguidance.data.*
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.IslamicTopBar
import com.example.ui.theme.AccentGold
import com.example.ui.theme.EmeraldPrimaryLight
import kotlin.math.abs

@Composable
fun PrayerTimesScreen(
    viewModel: IslamicViewModel,
    canNavigateBack: Boolean = true,
    onNavigateBack: () -> Unit = {},
    onNavigateToQibla: () -> Unit = {}
) {
    val settings by viewModel.settings.collectAsState()
    val schedule by viewModel.prayerSchedule.collectAsState()
    val language = settings.language

    var showCityDialog by remember { mutableStateOf(false) }

    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text(if (language == AppLanguage.BENGALI) "শহর নির্বাচন করুন" else "Select City") },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(PrayerCalculator.supportedCities.size) { index ->
                        val city = PrayerCalculator.supportedCities[index]
                        val isSelected = city.nameBn == settings.selectedCityName || city.nameEn == settings.selectedCityName
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectCity(city.nameBn)
                                    showCityDialog = false
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${city.nameBn} (${city.nameEn})",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text(AppStrings.get("cancel", language))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = AppStrings.get("prayer_schedule", language),
                subtitle = "${settings.selectedCityName} • বাংলাদেশ",
                canNavigateBack = canNavigateBack,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { showCityDialog = true }) {
                        Icon(Icons.Default.LocationCity, contentDescription = "Change City")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (schedule == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val s = schedule!!

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("prayer_times_screen"),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Header Card with Next Prayer Countdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(EmeraldPrimaryLight, Color(0xFF09432A))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = AppStrings.get("next_prayer", language),
                                        style = MaterialTheme.typography.labelSmall.copy(color = AccentGold)
                                    )
                                    Text(
                                        text = if (language == AppLanguage.BENGALI) s.nextPrayerNameBn else s.nextPrayerNameEn,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    val rem = if (language == AppLanguage.BENGALI) {
                                        "${PrayerCalculator.convertToBengaliNumerals(s.remainingHours)} ঘণ্টা ${PrayerCalculator.convertToBengaliNumerals(s.remainingMinutes)} মিনিট বাকি"
                                    } else {
                                        "${s.remainingHours}h ${s.remainingMinutes}m remaining"
                                    }
                                    Text(
                                        text = rem,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "${s.hijriDateBn}  |  ${s.gregorianDateBn}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.85f))
                            )
                        }
                    }
                }
            }

            // Qibla Compass Navigation Button
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToQibla),
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
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(AccentGold.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Explore, contentDescription = null, tint = AccentGold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = AppStrings.get("qibla_compass", language),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "কিবলার কোণ: ${s.qiblaBearing.toInt()}° (পশ্চিম-উত্তর-পশ্চিম)",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Timetable Rows
            item {
                Text(
                    text = "আজকের নামাজের সময়সূচি:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            item { PrayerScheduleRow(AppStrings.get("fajr", language), s.fajr, s.nextPrayerNameEn == "Fajr", Icons.Default.WbTwilight) }
            item { PrayerScheduleRow(AppStrings.get("sunrise", language), s.sunrise, false, Icons.Default.WbSunny) }
            item { PrayerScheduleRow(AppStrings.get("dhuhr", language), s.dhuhr, s.nextPrayerNameEn == "Dhuhr", Icons.Default.Brightness5) }
            item { PrayerScheduleRow(AppStrings.get("asr", language), s.asr, s.nextPrayerNameEn == "Asr", Icons.Default.Brightness6) }
            item { PrayerScheduleRow(AppStrings.get("maghrib", language), s.maghrib, s.nextPrayerNameEn == "Maghrib", Icons.Default.NightsStay) }
            item { PrayerScheduleRow(AppStrings.get("isha", language), s.isha, s.nextPrayerNameEn == "Isha", Icons.Default.Brightness2) }
        }
    }
}

@Composable
fun PrayerScheduleRow(name: String, time: String, isNext: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isNext) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 3.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Text(
                text = time,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun QiblaCompassScreen(
    viewModel: IslamicViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val settings by viewModel.settings.collectAsState()
    val schedule by viewModel.prayerSchedule.collectAsState()
    val azimuth by viewModel.compassSensor.azimuth.collectAsState()
    val hasSensor by viewModel.compassSensor.hasSensor.collectAsState()

    val targetBearing = schedule?.qiblaBearing ?: 279f
    // Relative angle of the Qibla needle from the top of the phone
    val needleAngle = (targetBearing - azimuth + 360) % 360
    val animatedNeedleAngle by animateFloatAsState(targetValue = needleAngle, label = "needle_rot")

    val isAligned = abs(azimuth - targetBearing) < 5f || abs(azimuth - targetBearing) > 355f

    Scaffold(
        topBar = {
            IslamicTopBar(
                title = "কিবলা কম্পাস",
                subtitle = "${settings.selectedCityName} থেকে মক্কার দিক",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .testTag("qibla_compass_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Status Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isAligned) EmeraldPrimaryLight else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAligned) "✓ আপনি ঠিক কিবলার দিকে মুখ করে আছেন!" else "ফোনটিকে ঘুরিয়ে তীরটি কাবার প্রতীকের সাথে মিলান",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = if (isAligned) Color.White else MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "কিবলার দিক: ${targetBearing.toInt()}° | বর্তমান দিক: ${azimuth.toInt()}°",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isAligned) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            // Compass Dial & Needle
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                // Outer Dial Drawing with cardinal directions
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    val cy = size.height / 2
                    val r = size.minDimension / 2 - 12.dp.toPx()

                    // Circular ring
                    drawCircle(
                        color = Color(0xFFD4AF37).copy(alpha = 0.4f),
                        radius = r,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )

                    // Degree tick marks
                    for (i in 0 until 360 step 30) {
                        val rad = Math.toRadians(i.toDouble())
                        val p1 = Offset(
                            (cx + (r - 10.dp.toPx()) * Math.cos(rad)).toFloat(),
                            (cy + (r - 10.dp.toPx()) * Math.sin(rad)).toFloat()
                        )
                        val p2 = Offset(
                            (cx + r * Math.cos(rad)).toFloat(),
                            (cy + r * Math.sin(rad)).toFloat()
                        )
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.5f),
                            start = p1,
                            end = p2,
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                // Rotated Qibla Needle (points toward Kaaba)
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .rotate(animatedNeedleAngle),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val cx = size.width / 2
                        val cy = size.height / 2

                        // Kaaba Pointer Needle (Green & Gold)
                        val needlePath = Path().apply {
                            moveTo(cx, cy - 90.dp.toPx()) // North/Kaaba tip
                            lineTo(cx + 14.dp.toPx(), cy)
                            lineTo(cx, cy - 14.dp.toPx())
                            lineTo(cx - 14.dp.toPx(), cy)
                            close()
                        }
                        drawPath(needlePath, color = if (isAligned) Color(0xFF0D5C3A) else Color(0xFFD4AF37))

                        // South Pointer (Grey)
                        val southPath = Path().apply {
                            moveTo(cx, cy + 90.dp.toPx())
                            lineTo(cx + 14.dp.toPx(), cy)
                            lineTo(cx, cy + 14.dp.toPx())
                            lineTo(cx - 14.dp.toPx(), cy)
                            close()
                        }
                        drawPath(southPath, color = Color.Gray.copy(alpha = 0.5f))

                        // Center Pivot
                        drawCircle(color = Color(0xFFD4AF37), radius = 10.dp.toPx())
                        drawCircle(color = Color(0xFF0D5C3A), radius = 6.dp.toPx())
                    }

                    // Kaaba Icon at top of needle
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🕋", fontSize = 18.sp)
                    }
                }
            }

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "কিবলা নির্দেশিকা:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "পবিত্র কাবা শরীফের ভৌগোলিক অবস্থান: ২১.৪২২৫° উত্তর, ৩৯.৮২৬২° পূর্ব। বাংলাদেশ থেকে কিবলার সাধারণ দিক পশ্চিম-উত্তর-পশ্চিম (প্রায় ২৭৯ ডিগ্রি)।",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }
    }
}
