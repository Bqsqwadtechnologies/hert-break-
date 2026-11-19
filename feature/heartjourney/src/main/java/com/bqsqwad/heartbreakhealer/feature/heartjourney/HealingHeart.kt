package com.bqsqwad.heartbreakhealer.feature.heartjourney

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bqsqwad.heartbreakhealer.core.domain.journey.JourneyState

private fun heartPath(w: Float, h: Float): Path = Path().apply {
    moveTo(w * 0.50f, h * 0.93f)
    cubicTo(w * 0.00f, h * 0.62f, w * 0.00f, h * 0.20f, w * 0.26f, h * 0.13f)
    cubicTo(w * 0.40f, h * 0.09f, w * 0.50f, h * 0.20f, w * 0.50f, h * 0.28f)
    cubicTo(w * 0.50f, h * 0.20f, w * 0.60f, h * 0.09f, w * 0.74f, h * 0.13f)
    cubicTo(w * 1.00f, h * 0.20f, w * 1.00f, h * 0.62f, w * 0.50f, h * 0.93f)
    close()
}

/** progress 0f = two broken halves far apart, 1f = whole heart with a bandage. Closes smoothly. */
@Composable
fun HealingHeart(progress: Float, modifier: Modifier = Modifier, reduceMotion: Boolean = false) {
    val target = progress.coerceIn(0f, 1f)
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = if (reduceMotion) snap() else tween(1100, easing = FastOutSlowInEasing),
        label = "heal",
    )
    val pulse = rememberInfiniteTransition(label = "pulse")
    val beat by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (reduceMotion || animated < 1f) 1f else 1.05f,
        animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
        label = "beat",
    )
    val red = MaterialTheme.colorScheme.primary
    val pink = MaterialTheme.colorScheme.tertiary
    val description = "Healing heart, ${(target * 100).toInt()} percent healed"
    Canvas(modifier.aspectRatio(1f).semantics { contentDescription = description }) {
        val w = size.width
        val h = size.height
        val heart = heartPath(w, h)
        val open = 1f - animated
        val gap = open * w * 0.13f
        val tilt = open * 8f
        val brush = Brush.linearGradient(listOf(pink, red), start = Offset(0f, 0f), end = Offset(w, h))
        val pivot = Offset(w * 0.5f, h * 0.93f)
        scale(beat, pivot = Offset(w / 2f, h / 2f)) {
            withTransform({ translate(-gap, 0f); rotate(-tilt, pivot) }) {
                clipRect(0f, 0f, w / 2f, h) { drawPath(heart, brush) }
            }
            withTransform({ translate(gap, 0f); rotate(tilt, pivot) }) {
                clipRect(w / 2f, 0f, w, h) { drawPath(heart, brush) }
            }
            // glowing crack, strongest when the heart is most broken
            if (open > 0.02f) {
                val crack = Path().apply {
                    moveTo(w * 0.50f, h * 0.22f); lineTo(w * 0.46f, h * 0.36f); lineTo(w * 0.54f, h * 0.48f)
                    lineTo(w * 0.47f, h * 0.62f); lineTo(w * 0.53f, h * 0.75f); lineTo(w * 0.50f, h * 0.92f)
                }
                drawPath(
                    crack, Color(0xFFFFE9A8).copy(alpha = open.coerceIn(0f, 1f) * 0.9f),
                    style = Stroke(width = w * 0.035f * (0.4f + open), cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
            }
            // bandage appears once the heart is more than half healed
            if (animated > 0.5f) {
                val a = ((animated - 0.5f) / 0.5f).coerceIn(0f, 1f)
                val band = Color(0xFFF6EBDD).copy(alpha = a)
                val bw = w * 0.62f
                val bh = h * 0.12f
                val c = Offset(w / 2f, h * 0.52f)
                rotate(-32f, c) { drawRoundRect(band, Offset(c.x - bw / 2f, c.y - bh / 2f), Size(bw, bh), CornerRadius(bh / 2f)) }
                rotate(32f, c) { drawRoundRect(band, Offset(c.x - bw / 2f, c.y - bh / 2f), Size(bw, bh), CornerRadius(bh / 2f)) }
                drawRoundRect(Color(0xFFFFFFFF).copy(alpha = 0.55f * a), Offset(c.x - bh * 0.6f, c.y - bh * 0.6f), Size(bh * 1.2f, bh * 1.2f), CornerRadius(bh * 0.25f))
            }
            // sparkles when completely healed
            if (animated > 0.98f) {
                val spark = Color(0xFFFFD866)
                listOf(0.16f to 0.20f, 0.84f to 0.24f, 0.10f to 0.55f, 0.92f to 0.58f, 0.30f to 0.05f, 0.72f to 0.04f).forEach { (x, y) ->
                    drawCircle(spark, w * 0.018f, Offset(w * x, h * y))
                }
            }
        }
    }
}

/** The card shown at the top of Home. Same look as the other Home cards. */
@Composable
fun HealingHeartCard(
    state: JourneyState,
    reduceMotion: Boolean,
    celebrating: Boolean,
    onOpen: () -> Unit,
    onBrokenAgain: () -> Unit,
) {
    val ink = Color(0xFF17172A)
    val muted = Color(0xFF666477)
    Card(
        onClick = onOpen,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                if (state.finished) "Your heart is healed 🌸" else "Your heart rebuild",
                style = MaterialTheme.typography.titleMedium, color = ink, fontWeight = FontWeight.Bold,
            )
            if (celebrating) {
                Text("We are celebrating you. Keep checking in regularly.", color = muted, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${state.percent}%", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Text("healed", style = MaterialTheme.typography.labelMedium, color = muted)
                }
                HealingHeart(state.heartProgress, Modifier.weight(1.5f), reduceMotion)
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${state.daysToGo}", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    Text("days to go", style = MaterialTheme.typography.labelMedium, color = muted, textAlign = TextAlign.Center)
                }
            }
            Spacer(Modifier.height(10.dp))
            if (!state.finished) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StepChip("Story", state.today.story, Modifier.weight(1f))
                    StepChip("Jokes ${state.today.jokes.coerceAtMost(2)}/2", state.today.jokesDone, Modifier.weight(1f))
                    StepChip("Rebuild", state.today.rebuildStep, Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                Button(onClick = onOpen, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                    Text("Open today's checklist")
                }
            } else {
                Button(onClick = onBrokenAgain, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                    Text("Broken again")
                }
            }
        }
    }
}

@Composable
internal fun StepChip(label: String, done: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (done) Color(0xFFE5F5EB) else Color(0xFFFFF0F3),
    ) {
        Text(
            (if (done) "✓ " else "○ ") + label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (done) Color(0xFF218B52) else Color(0xFF666477),
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}
