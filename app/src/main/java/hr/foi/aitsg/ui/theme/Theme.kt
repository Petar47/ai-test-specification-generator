package hr.foi.aitsg.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Cyan,
    secondary = White,
    onSecondary = LightGrey,
    tertiary = Grey,
    background = Black,
    inversePrimary = White
)

/* Other default colors to override
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,

onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/
private val LightColorScheme = lightColorScheme(
    primary = Cyan,
    secondary = White,
    onSecondary = LightGrey,
    tertiary = Grey,
    background = White,
    inversePrimary = Grey
)

@Composable
fun AITSGTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!darkTheme){
        LightColorScheme
    }else {
        DarkColorScheme
    }




    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
