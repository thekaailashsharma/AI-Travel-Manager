package ai.travel.app.tripDetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun VerticalDashedDivider(
    color: Color = Color.Black,
    height: Int = 100,
    dashWidth: Float = 4f,
    gapWidth: Float = 4f
) {
    Canvas(modifier = Modifier.height(height.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, gapWidth), 0f)
        drawLine(
            color = color,
            start = Offset(size.width / 2, 0f),
            end = Offset(size.width / 2, size.height),
            pathEffect = pathEffect
        )
    }
}