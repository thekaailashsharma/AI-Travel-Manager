package ai.travel.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val appGradient: Brush
    @Composable
    get() = Brush.verticalGradient(
        0.0f to Color(0xFF231d47),
//        150.0f to Color(0xFF271c4d),
//        200.0f to Color(0xFF142742),
//        300.0f to Color(0xFF1b325d),
//        400.0f to Color(0xFF1b304d),
        500.0f to Color(0xFF1b304d),
    )

val igGradient: Brush
    @Composable
    get() = Brush.verticalGradient(
        0.0f to Color(0xFFe60396),
//        150.0f to Color(0xFF271c4d),
//        200.0f to Color(0xFF142742),
//        300.0f to Color(0xFF1b325d),
//        400.0f to Color(0xFF1b304d),
        500.0f to Color(0xFFffa22e),
    )



val textColor = Color(0xFFE0E0E0)
val lightText = Color(0xFF3f68a8)

val bottomBarBackground = Color(0xFF172749)
val bottomBarBorder = Color(0xFF3c69a8)
val CardBackground = Color(0xFF122754)

val borderBrush = Brush.radialGradient(
    listOf(
        bottomBarBorder,
        Color.White,
        bottomBarBorder,
        Color.White,
        bottomBarBorder,
    )
)

val isDarkThemeEnabled : Boolean
    @Composable
    get() = isSystemInDarkTheme()

val P2PBackground: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color(0xFFd7e1e8) else Color(0xFFd7e1e8)

val TextColor: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color.Black else Color.Black
