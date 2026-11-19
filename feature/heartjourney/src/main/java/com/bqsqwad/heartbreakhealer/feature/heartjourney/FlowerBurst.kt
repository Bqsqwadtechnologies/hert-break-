package com.bqsqwad.heartbreakhealer.feature.heartjourney

import android.provider.Settings
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private class Petal(val vx: Float, val vy: Float, val size: Float, val color: Color, val spin: Float, val delay: Float)

private val palette = listOf(Color(0xFFFF8AA0), Color(0xFFFFB3C1), Color(0xFFFFFFFF), Color(0xFFFFD866), Color(0xFFE51D3E), Color(0xFFB39DDB))

/** 4.5 seconds of flowers falling and spreading from the top of the screen. It does not block any touch. */
@Composable
fun FlowerBurst(onFinished: () -> Unit) {
    val petals = remember {
        val r = Random(7)
        List(110) {
            Petal(vx = (r.nextFloat() - 0.5f) * 1.8f, vy = 0.5f + r.nextFloat() * 1.1f, size = 9f + r.nextFloat() * 15f,
                color = palette[r.nextInt(palette.size)], spin = r.nextFloat() * 360f, delay = r.nextFloat() * 0.6f)
        }
    }
    var t by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        val start = withFrameNanos { it }
        while (t < 4.5f) withFrameNanos { now -> t = (now - start) / 1_000_000_000f }
        onFinished()
    }
    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        petals.forEach { p ->
            val tt = (t - p.delay).coerceAtLeast(0f)
            if (tt <= 0f) return@forEach
            val x = w * 0.5f + p.vx * w * 0.32f * tt
            val y = h * 0.04f + p.vy * h * 0.30f * tt
            val alpha = (1f - ((t - 3.2f) / 1.3f).coerceIn(0f, 1f))
            if (y > h + 40f || alpha <= 0f) return@forEach
            repeat(5) { i ->
                val a = Math.toRadians((p.spin + i * 72f + t * 120f).toDouble())
                drawCircle(p.color.copy(alpha = alpha), p.size * 0.45f, Offset(x + (cos(a) * p.size * 0.5f).toFloat(), y + (sin(a) * p.size * 0.5f).toFloat()))
            }
            drawCircle(Color(0xFFFFC107).copy(alpha = alpha), p.size * 0.25f, Offset(x, y))
        }
    }
}

/** Placed once above the whole app. Plays the burst the moment day 30 is counted, on whatever screen the user is on. */
@Composable
fun HeartJourneyGlobalOverlay(vm: HeartJourneyViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    val appReduce by vm.reduceMotion.collectAsState()
    val context = LocalContext.current
    val systemReduce = remember { Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f }
    val pending = state?.celebrationPending == true
    var played by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(pending) { if (!pending) played = false }
    if (pending && !played && !appReduce && !systemReduce) FlowerBurst(onFinished = { played = true })
}
