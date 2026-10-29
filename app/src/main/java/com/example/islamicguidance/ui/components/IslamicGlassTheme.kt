package com.example.islamicguidance.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentGold
import com.example.ui.theme.IslamicGold

enum class GlassIconType {
    HOME,
    QURAN,
    HADITH,
    AZKAR,
    DUAS,
    TASBIH,
    PRAYER_TIMES,
    BOOKMARKS,
    DOWNLOADS,
    SETTINGS
}

object IslamicGlassTokens {
    val LightGlassBackground = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.82f),
            Color(0xFFE8F5E9).copy(alpha = 0.65f)
        )
    )

    val DarkGlassBackground = Brush.linearGradient(
        colors = listOf(
            Color(0xFF14281E).copy(alpha = 0.85f),
            Color(0xFF0D1B14).copy(alpha = 0.70f)
        )
    )

    val LightGlassBorder = Color.White.copy(alpha = 0.60f)
    val DarkGlassBorder = Color(0xFF38D38E).copy(alpha = 0.35f)

    val GoldGlassBorder = IslamicGold.copy(alpha = 0.45f)
}

/**
 * Modifier to render a sleek liquid frosted glass effect with border stroke and rounded corners.
 */
fun Modifier.liquidGlassEffect(
    shape: Shape = RoundedCornerShape(18.dp),
    isDark: Boolean = false,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        brush = if (isDark) IslamicGlassTokens.DarkGlassBackground else IslamicGlassTokens.LightGlassBackground,
        shape = shape
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = if (isDark) {
                listOf(IslamicGlassTokens.DarkGlassBorder, Color.White.copy(alpha = 0.15f))
            } else {
                listOf(IslamicGlassTokens.LightGlassBorder, IslamicGlassTokens.GoldGlassBorder)
            }
        ),
        shape = shape
    )

/**
 * Custom Liquid Glass Icon with multi-layer refraction for the 10 core features.
 */
@Composable
fun LiquidGlassIcon(
    type: GlassIconType,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    containerSize: Dp = 44.dp
) {
    val iconVector: ImageVector = when (type) {
        GlassIconType.HOME -> Icons.Default.Home
        GlassIconType.QURAN -> Icons.Default.MenuBook
        GlassIconType.HADITH -> Icons.Default.LibraryBooks
        GlassIconType.AZKAR -> Icons.Default.Favorite
        GlassIconType.DUAS -> Icons.Default.VolunteerActivism
        GlassIconType.TASBIH -> Icons.Default.TouchApp
        GlassIconType.PRAYER_TIMES -> Icons.Default.AccessTime
        GlassIconType.BOOKMARKS -> Icons.Default.Bookmark
        GlassIconType.DOWNLOADS -> Icons.Default.CloudDownload
        GlassIconType.SETTINGS -> Icons.Default.Settings
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .size(containerSize)
            .clip(CircleShape)
            .background(
                brush = if (isSelected) {
                    Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.15f)
                        )
                    )
                } else {
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.20f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    )
                }
            )
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) primaryColor else Color.White.copy(alpha = 0.40f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            tint = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(iconSize)
        )
    }
}
