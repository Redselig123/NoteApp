package com.example.noteapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF1565C0),     // Azul fuerte
    onPrimary = Color.White,
    secondary = Color(0xFF26A69A),   // Verde azulado
    onSecondary = Color.White,
    background = Color(0xFFFDFDFD),  // Fondo casi blanco
    onBackground = Color(0xFF212121),
    surface = Color.White,
    onSurface = Color(0xFF212121),
    error = Color(0xFFD32F2F),
    onError = Color.White
)

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF0D47A1),     // Azul profundo (para AppBar, si querés)
    onPrimary = Color.White,
    secondary = Color(0xFF80CBC4),   // Verde azulado (queda libre, no lo usamos en diálogos)
    onSecondary = Color.Black,
    background = Color(0xFF121212),  // Fondo general
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF1E1E1E),     // Fondo diálogos, cards
    onSurface = Color(0xFFE0E0E0),   // Texto principal y botones en blanco
    error = Color(0xFFEF5350),
    onError = Color.Black
)


@Composable
fun NoteAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme){
        DarkColorPalette
    }else{
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}