package com.example.pokedex_hacksprint_2025.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Red200,
    onPrimary = White,
    background = EigenGrau,
    surface = EigenGrau,
    onBackground = White,
    onSurface = White,
)

private val LightColorScheme = lightColorScheme(
    primary = Red500,
    onPrimary = EigenGrau,
    background = White,
    surface = White,
    onBackground = EigenGrau,
    onSurface = EigenGrau,
)

//isSystemInDarkTheme()
@Composable
fun PokeDex_Hacksprint_2025Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground) // 🔥 Garante que TODOS os Text() sigam o tema
        ) {
            content()
        }
    }
}
