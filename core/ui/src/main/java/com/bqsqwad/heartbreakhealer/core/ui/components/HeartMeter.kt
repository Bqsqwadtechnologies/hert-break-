package com.bqsqwad.heartbreakhealer.core.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.dp

@Composable
fun HeartMeter(
    progress: Float,
    modifier: Modifier = Modifier,
    reduceMotion: Boolean = false,
    label: String? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "heartPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (reduceMotion) 1f else 1.04f,
        animationSpec = infiniteRepeatable(animation = tween(1200), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )
    val fillColor = MaterialTheme.colorScheme.tertiary
    val outlineColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier.fillMaxWidth(0.55f).aspectRatio(1f).padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            val w = size.width
            val h = size.height
            val path = createHeartPath(w, h)
            drawPath(path = path, color = outlineColor, style = Stroke(width = 6.dp.toPx()))
            clipPath(path) {
                val fillHeight = h * progress.coerceIn(0f, 1f)
                drawRect(
                    color = fillColor.copy(alpha = 0.85f),
                    topLeft = Offset(0f, h - fillHeight),
                    size = Size(w, fillHeight)
                )
            }
        }
        if (label != null) {
            Text(text = label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

private fun createHeartPath(width: Float, height: Float): Path {
    val path = Path()
    val w = width
    val h = height
    path.moveTo(w / 2f, h * 0.35f)
    path.cubicTo(w * 0.15f, h * 0.05f, -w * 0.1f, h * 0.45f, w / 2f, h * 0.95f)
    path.cubicTo(w * 1.1f, h * 0.45f, w * 0.85f, h * 0.05f, w / 2f, h * 0.35f)
    path.close()
    return path
}
