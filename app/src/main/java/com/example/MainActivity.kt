package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.islamicguidance.data.ThemeMode
import com.example.islamicguidance.ui.IslamicViewModel
import com.example.islamicguidance.ui.components.FloatingAudioBar
import com.example.islamicguidance.ui.components.IslamicBottomNavigationBar
import com.example.islamicguidance.ui.components.NavigationTab
import com.example.islamicguidance.ui.screens.*
import com.example.ui.theme.IslamicGuidanceTheme

sealed class Screen {
    data class Tab(val tab: NavigationTab) : Screen()
    data class SurahDetail(val surahNumber: Int) : Screen()
    object Duas : Screen()
    object Tasbih : Screen()
    object PrayerTimes : Screen()
    object QiblaCompass : Screen()
    object Bookmarks : Screen()
    object Search : Screen()
    object Themes : Screen()
    object Reciters : Screen()
    object AudioDownloads : Screen()
}

class MainActivity : ComponentActivity() {

    private val viewModel: IslamicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val settings by viewModel.settings.collectAsState()
            val isDarkTheme = when (settings.themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Tab(NavigationTab.HOME)) }

            IslamicGuidanceTheme(
                themePreset = settings.themePreset,
                darkTheme = isDarkTheme
            ) {
                val isSubScreen = currentScreen !is Screen.Tab

                // Back navigation handler
                BackHandler(enabled = isSubScreen) {
                    currentScreen = Screen.Tab(NavigationTab.HOME)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (!isSubScreen) {
                            val activeTab = (currentScreen as Screen.Tab).tab
                            IslamicBottomNavigationBar(
                                currentTab = activeTab,
                                onTabSelected = { tab ->
                                    currentScreen = Screen.Tab(tab)
                                },
                                language = settings.language,
                                isGlassTheme = settings.themePreset == com.example.ui.theme.IslamicThemePreset.LIQUID_GLASS
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (val screen = currentScreen) {
                            is Screen.Tab -> {
                                when (screen.tab) {
                                    NavigationTab.HOME -> HomeScreen(
                                        viewModel = viewModel,
                                        onNavigateToQuran = { currentScreen = Screen.Tab(NavigationTab.QURAN) },
                                        onNavigateToSurah = { surahNum ->
                                            viewModel.selectSurahByNumber(surahNum)
                                            currentScreen = Screen.SurahDetail(surahNum)
                                        },
                                        onNavigateToHadith = { currentScreen = Screen.Tab(NavigationTab.HADITH) },
                                        onNavigateToAzkar = { currentScreen = Screen.Tab(NavigationTab.AZKAR) },
                                        onNavigateToDuas = { currentScreen = Screen.Duas },
                                        onNavigateToTasbih = { currentScreen = Screen.Tasbih },
                                        onNavigateToPrayer = { currentScreen = Screen.PrayerTimes },
                                        onNavigateToQibla = { currentScreen = Screen.QiblaCompass },
                                        onNavigateToSearch = { currentScreen = Screen.Search }
                                    )
                                    NavigationTab.QURAN -> QuranListScreen(
                                        viewModel = viewModel,
                                        onSurahSelected = { surah ->
                                            viewModel.selectSurah(surah)
                                            currentScreen = Screen.SurahDetail(surah.number)
                                        }
                                    )
                                    NavigationTab.HADITH -> HadithScreen(viewModel = viewModel)
                                    NavigationTab.AZKAR -> AzkarScreen(viewModel = viewModel)
                                    NavigationTab.SETTINGS -> SettingsScreen(
                                        viewModel = viewModel,
                                        onNavigateToBookmarks = { currentScreen = Screen.Bookmarks },
                                        onNavigateToThemes = { currentScreen = Screen.Themes },
                                        onNavigateToReciters = { currentScreen = Screen.Reciters },
                                        onNavigateToDownloads = { currentScreen = Screen.AudioDownloads }
                                    )
                                }
                            }

                            is Screen.SurahDetail -> SurahDetailScreen(
                                viewModel = viewModel,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.QURAN) }
                            )

                            is Screen.Duas -> DuasScreen(
                                viewModel = viewModel,
                                canNavigateBack = true,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.HOME) }
                            )

                            is Screen.Tasbih -> TasbihScreen(
                                viewModel = viewModel,
                                canNavigateBack = true,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.HOME) }
                            )

                            is Screen.PrayerTimes -> PrayerTimesScreen(
                                viewModel = viewModel,
                                canNavigateBack = true,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.HOME) },
                                onNavigateToQibla = { currentScreen = Screen.QiblaCompass }
                            )

                            is Screen.QiblaCompass -> QiblaCompassScreen(
                                viewModel = viewModel,
                                onNavigateBack = { currentScreen = Screen.PrayerTimes }
                            )

                            is Screen.Bookmarks -> BookmarksScreen(
                                viewModel = viewModel,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.SETTINGS) }
                            )

                            is Screen.Search -> SearchScreen(
                                viewModel = viewModel,
                                onNavigateToSurah = { surahNum ->
                                    viewModel.selectSurahByNumber(surahNum)
                                    currentScreen = Screen.SurahDetail(surahNum)
                                },
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.HOME) }
                            )

                            is Screen.Themes -> IslamicThemesScreen(
                                viewModel = viewModel,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.SETTINGS) }
                            )

                            is Screen.Reciters -> RecitersScreen(
                                viewModel = viewModel,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.SETTINGS) }
                            )

                            is Screen.AudioDownloads -> AudioDownloadsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { currentScreen = Screen.Tab(NavigationTab.SETTINGS) },
                                onNavigateToReciters = { currentScreen = Screen.Reciters }
                            )
                        }

                        // Floating Audio Recitation Player Bar
                        FloatingAudioBar(
                            viewModel = viewModel,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = if (isSubScreen) 12.dp else 4.dp)
                        )
                    }
                }
            }
        }
    }
}
