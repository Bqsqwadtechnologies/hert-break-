package com.bqsqwad.heartbreakhealer.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import androidx.core.view.ViewCompat
import com.bqsqwad.heartbreakhealer.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AnimatedSplashScreen(
    reduceMotion: Boolean = false,
    isFirstLaunch: Boolean = true,
    hapticsEnabled: Boolean = true,
    onFinished: () -> Unit
) {
    val view = LocalView.current
    var phase by remember { mutableStateOf(0) }
    val heartAlpha = remember { Animatable(0f) }
    val heartScale = remember { Animatable(0.92f) }
    val textAlpha = remember { Animatable(0f) }
    val sparkleProgress = remember { Animatable(0f) }
    var finished by remember { mutableStateOf(false) }

    fun skipToEnd() {
        if (!finished) {
            finished = true
            onFinished()
        }
    }

    fun haptic() {
        if (hapticsEnabled && !reduceMotion) {
            ViewCompat.performHapticFeedback(view, HapticFeedbackConstantsCompat.CLOCK_TICK)
        }
    }

    LaunchedEffect(reduceMotion, isFirstLaunch) {
        if (reduceMotion) {
            heartAlpha.animateTo(1f, tween(400))
            heartScale.snapTo(1f)
            phase = 2
            delay(300)
            textAlpha.animateTo(1f, tween(400))
            delay(700)
            skipToEnd()
            return@LaunchedEffect
        }
        if (isFirstLaunch) {
            phase = 0
            heartAlpha.animateTo(1f, tween(500))
            heartScale.animateTo(1f, tween(600))
            delay(600)
            haptic()
            phase = 1
            delay(700)
            haptic()
            phase = 2
            sparkleProgress.animateTo(1f, tween(600))
            delay(600)
            phase = 3
            textAlpha.animateTo(1f, tween(400))
            delay(600) // final healed heart for 0.6s
        } else {
            phase = 1
            heartAlpha.snapTo(1f)
            heartScale.snapTo(1f)
            delay(400)
            haptic()
            phase = 2
            sparkleProgress.animateTo(1f, tween(400))
            delay(400)
            phase = 3
            textAlpha.animateTo(1f, tween(300))
            delay(400)
        }
        skipToEnd()
    }

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val bgBrush = if (isDark) {
        Brush.verticalGradient(listOf(Color(0xFF0A0E21), Color(0xFF1A1035)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0F4FF)))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { skipToEnd() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .graphicsLayer {
                        scaleX = heartScale.value
                        scaleY = heartScale.value
                        alpha = heartAlpha.value
                    }
                    .semantics { contentDescription = "Heartbreak Healer logo, a healed heart" },
                contentAlignment = Alignment.Center
            ) {
                val res = when (phase) {
                    0 -> R.drawable.heart_stage1
                    1 -> R.drawable.heart_stage2
                    else -> R.drawable.heart_stage3
                }
                Image(
                    painter = painterResource(res),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                if (phase >= 2 && !reduceMotion && sparkleProgress.value > 0f) {
                    SparkleCanvas(progress = sparkleProgress.value)
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 26.sp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(textAlpha.value)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(textAlpha.value)
            )
        }
    }
}

@Composable
private fun SparkleCanvas(progress: Float) {
    val particles = remember {
        List(36) {
            Sparkle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = 2f + Random.nextFloat() * 5f,
                speed = 0.3f + Random.nextFloat() * 0.7f,
                gold = Random.nextBoolean()
            )
        }
    }
    Canvas(modifier = Modifier.size(220.dp)) {
        val cx = size.width / 2
        val cy = size.height / 2
        particles.forEach { p ->
            val t = (progress * p.speed).coerceIn(0f, 1f)
            val alpha = (1f - t) * 0.9f
            val radius = p.size * (0.5f + t * 1.5f)
            val dx = (p.x - 0.5f) * size.width * t * 1.4f
            val dy = (p.y - 0.5f) * size.height * t * 1.4f
            drawCircle(
                color = if (p.gold) Color(0xFFFFD54F).copy(alpha = alpha) else Color.White.copy(alpha = alpha),
                radius = radius,
                center = Offset(cx + dx, cy + dy)
            )
        }
    }
}

private data class Sparkle(val x: Float, val y: Float, val size: Float, val speed: Float, val gold: Boolean)

private fun Color.luminance(): Float {
    val r = red
    val g = green
    val b = blue
    return 0.299f * r + 0.587f * g + 0.114f * b
}
