package com.trozovka.pocketvdr.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Trozovka maritime apps: one calm dark look (easy on the eyes on a dark bridge).
// Teal = on / OK / navigation bar. Orange = attention only (alarm, stop, problems).
val TealBar = Color(0xFF00695C)
val Teal = Color(0xFF26A69A)
val Orange = Color(0xFFF4511E)
val Slate = Color(0xFF263238)
val SlateRaised = Color(0xFF2E3C43)
val OnDark = Color(0xFFECEFF1)
val OnDarkMuted = Color(0xFFB0BEC5)

private val TrozovkaDark = darkColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    secondary = Teal,
    onSecondary = Color.White,
    tertiary = Orange,
    background = Slate,
    onBackground = OnDark,
    surface = Slate,
    onSurface = OnDark,
    surfaceVariant = SlateRaised,
    onSurfaceVariant = OnDarkMuted,
    error = Orange,
    onError = Color.White,
    outline = Color(0xFF546E7A),
)

@Composable
fun TrozovkaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = TrozovkaDark, content = content)
}
