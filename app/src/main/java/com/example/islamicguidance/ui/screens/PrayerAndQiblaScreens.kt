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
    var showMonthlyTimetable by remember { mutableStateOf(false) }
    var showMethodDialog by remember { mutableStateOf(false) }
    var showAsrDialog by remember { mutableStateOf(false) }

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
                subtitle = "${settings.selectedCityName} • ${schedule?.state ?: "West Bengal"}, ${schedule?.country ?: "ভারত"}",
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

            // Location & Coordinates Information Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == AppLanguage.BENGALI) "নামাজের অবস্থান" else "Prayer Location",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            TextButton(onClick = { showCityDialog = true }) {
                                Text(if (language == AppLanguage.BENGALI) "পরিবর্তন" else "Change")
                            }
                        }

                        Text(
                            text = "${settings.selectedCityName}, ${s.state}, ${s.country}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (settings.selectedCityName.contains("গার্ডেন রিচ") || settings.selectedCityName.contains("Garden Reach")) {
                                "অক্ষাংশ: ২২.৫৩৫০° উ, দ্রাঘিমাংশ: ৮৮.৩০৫০° পূ • Asia/Kolkata (IST +5:30)\n✓ ম্যানুয়াল অবস্থান: কোনো GPS পারমিশনের প্রয়োজন নেই।"
                            } else {
                                "অঞ্চল: ${s.country} • স্থানীয় সময় অঞ্চল ভিত্তিক অফলাইন হিসাব"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }

            // Calculation & Juristic Method Configuration Chips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { showMethodDialog = true },
                        label = {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "পদ্ধতি: করাচি (১৮°/১৮°)" else "Method: Karachi",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )

                    AssistChip(
                        onClick = { showAsrDialog = true },
                        label = {
                            Text(
                                text = if (settings.asrMethodId == "hanafi") {
                                    if (language == AppLanguage.BENGALI) "আসর: হানাফী (২x)" else "Asr: Hanafi (2x)"
                                } else {
                                    if (language == AppLanguage.BENGALI) "আসর: শাফেয়ী (১x)" else "Asr: Shafi'i (1x)"
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.HourglassBottom, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Monthly Timetable & Qibla Action Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showMonthlyTimetable = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (language == AppLanguage.BENGALI) "মাসিক সময়সূচি" else "Monthly Table")
                    }

                    OutlinedButton(
                        onClick = onNavigateToQibla,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(AppStrings.get("qibla_compass", language))
                    }
                }
            }

            // Timetable Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "আজকের নামাজের সময়সূচি:" else "Today's Prayer Schedule:",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = if (language == AppLanguage.BENGALI) "নোটিফিকেশন" else "Alerts",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            item {
                PrayerScheduleRow(
                    name = AppStrings.get("fajr", language),
                    time = s.fajr,
                    isNext = s.nextPrayerNameEn == "Fajr",
                    icon = Icons.Default.WbTwilight,
                    isAlertEnabled = settings.fajrNotification,
                    onToggleAlert = { viewModel.togglePrayerNotification("fajr", !settings.fajrNotification) }
                )
            }
            item {
                PrayerScheduleRow(
                    name = AppStrings.get("sunrise", language),
                    time = s.sunrise,
                    isNext = false,
                    icon = Icons.Default.WbSunny,
                    isAlertEnabled = false,
                    onToggleAlert = null
                )
            }
            item {
                PrayerScheduleRow(
                    name = AppStrings.get("dhuhr", language),
                    time = s.dhuhr,
                    isNext = s.nextPrayerNameEn == "Dhuhr",
                    icon = Icons.Default.Brightness5,
                    isAlertEnabled = settings.dhuhrNotification,
                    onToggleAlert = { viewModel.togglePrayerNotification("dhuhr", !settings.dhuhrNotification) }
                )
            }
            item {
                PrayerScheduleRow(
                    name = AppStrings.get("asr", language),
                    time = s.asr,
                    isNext = s.nextPrayerNameEn == "Asr",
                    icon = Icons.Default.Brightness6,
                    isAlertEnabled = settings.asrNotification,
                    onToggleAlert = { viewModel.togglePrayerNotification("asr", !settings.asrNotification) }
                )
            }
            item {
                PrayerScheduleRow(
                    name = AppStrings.get("maghrib", language),
                    time = s.maghrib,
                    isNext = s.nextPrayerNameEn == "Maghrib",
                    icon = Icons.Default.NightsStay,
                    isAlertEnabled = settings.maghribNotification,
                    onToggleAlert = { viewModel.togglePrayerNotification("maghrib", !settings.maghribNotification) }
                )
            }
            item {
                PrayerScheduleRow(
                    name = AppStrings.get("isha", language),
                    time = s.isha,
                    isNext = s.nextPrayerNameEn == "Isha",
                    icon = Icons.Default.Brightness2,
                    isAlertEnabled = settings.ishaNotification,
                    onToggleAlert = { viewModel.togglePrayerNotification("isha", !settings.ishaNotification) }
                )
            }

            // Accuracy Disclaimer Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = AccentGold,
                            modifier = Modifier.size(20.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (language == AppLanguage.BENGALI) {
                                "জরুরি সতর্কতা: এই সময়সূচি জ্যোতির্বৈজ্ঞানিক সূত্রের মাধ্যমে নির্ভুলভাবে গণনা করা হয়েছে। স্থানীয় মসজিদের নির্ধারিত আজান ও জামায়াতের সময়ের সাথে সুরক্ষামূলক সতর্কতার কারণে ১-২ মিনিট পার্থক্য হতে পারে।"
                            } else {
                                "Note: These prayer times are calculated mathematically from solar astronomical formulas. They may differ slightly (1-2 mins) from your local mosque timetable due to safety margins."
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 17.sp
                            )
                        )
                    }
                }
            }
        }
    }

    // Monthly Timetable Dialog
    if (showMonthlyTimetable && schedule != null) {
        val s = schedule!!
        AlertDialog(
            onDismissRequest = { showMonthlyTimetable = false },
            title = {
                Column {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "মাসিক নামাজের সময়সূচি" else "Monthly Prayer Timetable",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${settings.selectedCityName} • ${s.gregorianDateEn.substringAfter(" ")}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(s.monthlyDays.size) { idx ->
                        val day = s.monthlyDays[idx]
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (day.isToday) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.BENGALI) "${day.dateFormattedBn} (${day.hijriFormattedBn})" else "${day.dateFormattedEn}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (day.isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    if (day.isToday) {
                                        Text(
                                            text = if (language == AppLanguage.BENGALI) "আজ" else "Today",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("ফজর: ${day.fajr}", style = MaterialTheme.typography.bodySmall)
                                    Text("যোহর: ${day.dhuhr}", style = MaterialTheme.typography.bodySmall)
                                    Text("আসর: ${day.asr}", style = MaterialTheme.typography.bodySmall)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("সূর্যোদয়: ${day.sunrise}", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                    Text("মাগরিব: ${day.maghrib}", style = MaterialTheme.typography.bodySmall)
                                    Text("ইশা: ${day.isha}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMonthlyTimetable = false }) {
                    Text(if (language == AppLanguage.BENGALI) "বন্ধ করুন" else "Close")
                }
            }
        )
    }

    // Calculation Method Selection Dialog
    if (showMethodDialog) {
        AlertDialog(
            onDismissRequest = { showMethodDialog = false },
            title = { Text(if (language == AppLanguage.BENGALI) "হিসাব পদ্ধতি নির্বাচন" else "Select Calculation Method") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalculationMethod.entries.forEach { method ->
                        val isSelected = method.id == settings.calculationMethodId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setCalculationMethod(method.id)
                                    showMethodDialog = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (language == AppLanguage.BENGALI) method.nameBn else method.nameEn,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "Fajr: ${method.fajrAngle}° | Isha: ${method.ishaAngle}°",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMethodDialog = false }) {
                    Text(AppStrings.get("cancel", language))
                }
            }
        )
    }

    // Asr Method Selection Dialog
    if (showAsrDialog) {
        AlertDialog(
            onDismissRequest = { showAsrDialog = false },
            title = { Text(if (language == AppLanguage.BENGALI) "আসর মাযহাব পদ্ধতি" else "Asr Juristic Method") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AsrJuristicMethod.entries.forEach { asr ->
                        val isSelected = asr.id == settings.asrMethodId
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAsrMethod(asr.id)
                                    showAsrDialog = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (language == AppLanguage.BENGALI) asr.nameBn else asr.nameEn,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAsrDialog = false }) {
                    Text(AppStrings.get("cancel", language))
                }
            }
        )
    }
}

@Composable
fun PrayerScheduleRow(
    name: String,
    time: String,
    isNext: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isAlertEnabled: Boolean = false,
    onToggleAlert: (() -> Unit)? = null
) {
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                )
                if (onToggleAlert != null) {
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = onToggleAlert,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isAlertEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = "Toggle Prayer Alert",
                            tint = if (isAlertEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
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
