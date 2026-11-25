package com.bqsqwad.heartbreakhealer.feature.dashboard

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig
import com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

data class SoundscapeItem(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isPremium: Boolean,
    val tag: String
)

val SOUNDSCAPE_CATALOG = listOf(
    SoundscapeItem(
        id = "rain",
        title = "Gentle Rain & Soft Mist",
        description = "Continuous soothing rain to hush intrusive thoughts and ease anxiety.",
        emoji = "🌧️",
        isPremium = false,
        tag = "Anxiety Release"
    ),
    SoundscapeItem(
        id = "ocean",
        title = "Ocean Tide Swell",
        description = "Slow rhythmic waves matching your deep diaphragmatic breathing.",
        emoji = "🌊",
        isPremium = false,
        tag = "Deep Calm"
    ),
    SoundscapeItem(
        id = "healing_432",
        title = "432Hz Solfeggio Resonance",
        description = "Warm acoustic harmonic frequency for emotional grounding and release.",
        emoji = "✨",
        isPremium = false,
        tag = "Heart Healing"
    ),
    SoundscapeItem(
        id = "fireside",
        title = "Cozy Fireside Ember",
        description = "Comforting hearth warmth with gentle wood crackles for peaceful solitude.",
        emoji = "🔥",
        isPremium = false,
        tag = "Warm Comfort"
    ),
    SoundscapeItem(
        id = "theta_waves",
        title = "6Hz Theta Wave Reset",
        description = "Binaural oscillation engineered to soothe the nervous system and calm hyperarousal.",
        emoji = "🧠",
        isPremium = true,
        tag = "Nervous System"
    ),
    SoundscapeItem(
        id = "forest_sanctuary",
        title = "Restorative Forest Sanctuary",
        description = "Morning pine breeze and subtle distant chirps for emotional clarity.",
        emoji = "🌲",
        isPremium = true,
        tag = "Clarity & Peace"
    ),
    SoundscapeItem(
        id = "celestial_drone",
        title = "Celestial Twilight Ambient",
        description = "Transcendent harmonic drone for nighttime letting go and meditation.",
        emoji = "🌌",
        isPremium = true,
        tag = "Letting Go"
    ),
    SoundscapeItem(
        id = "delta_sleep",
        title = "Delta Deep Sleep Frequency",
        description = "Low-frequency acoustic wave designed to ease you into unbroken restorative sleep.",
        emoji = "🌙",
        isPremium = true,
        tag = "Deep Rest"
    )
)

/**
 * Lightweight pure-Kotlin procedural audio synthesizer using AudioTrack.
 * 100% offline, zero external dependencies, no large audio downloads.
 */
class SoundscapeAudioEngine {
    private var audioTrack: AudioTrack? = null
    private var synthesisJob: Job? = null
    private val sampleRate = 44100
    private var currentSoundId: String? = null
    private var volume = 0.75f

    fun setVolume(vol: Float) {
        volume = vol.coerceIn(0f, 1f)
    }

    fun play(soundId: String, scope: kotlinx.coroutines.CoroutineScope) {
        if (currentSoundId == soundId && synthesisJob?.isActive == true) return
        stop()
        currentSoundId = soundId

        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufSize = (minBuf * 4).coerceAtLeast(4096)

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(bufSize)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        audioTrack = track
        track.play()

        synthesisJob = scope.launch(Dispatchers.Default) {
            val chunk = ShortArray(1024)
            var phase = 0.0
            var phase2 = 0.0
            var noiseFilter = 0.0
            var lfoPhase = 0.0

            while (isActive) {
                val vol = volume
                for (i in chunk.indices) {
                    val sample = when (soundId) {
                        "rain" -> {
                            val white = Random.nextDouble() * 2.0 - 1.0
                            noiseFilter = noiseFilter * 0.90 + white * 0.10
                            noiseFilter * vol * 22000.0
                        }
                        "ocean" -> {
                            lfoPhase += 2.0 * PI * 0.10 / sampleRate
                            val swell = (sin(lfoPhase) + 1.0) * 0.5
                            val white = Random.nextDouble() * 2.0 - 1.0
                            noiseFilter = noiseFilter * 0.88 + white * 0.12
                            noiseFilter * (0.25 + 0.75 * swell) * vol * 24000.0
                        }
                        "healing_432" -> {
                            phase += 2.0 * PI * 432.0 / sampleRate
                            val s = sin(phase) * 0.7 + sin(phase * 2.0) * 0.18 + sin(phase * 0.5) * 0.12
                            s * vol * 18000.0
                        }
                        "fireside" -> {
                            noiseFilter = noiseFilter * 0.94 + (Random.nextDouble() * 2.0 - 1.0) * 0.06
                            var s = noiseFilter * 0.5
                            if (Random.nextDouble() < 0.0015) {
                                s += (Random.nextDouble() * 2.0 - 1.0) * 0.6
                            }
                            s * vol * 22000.0
                        }
                        "theta_waves" -> {
                            phase += 2.0 * PI * 216.0 / sampleRate
                            phase2 += 2.0 * PI * 6.0 / sampleRate
                            val carrier = sin(phase)
                            val beat = 0.6 + 0.4 * sin(phase2)
                            carrier * beat * vol * 20000.0
                        }
                        "forest_sanctuary" -> {
                            lfoPhase += 2.0 * PI * 0.15 / sampleRate
                            val white = Random.nextDouble() * 2.0 - 1.0
                            noiseFilter = noiseFilter * 0.92 + white * 0.08
                            phase += 2.0 * PI * 600.0 / sampleRate
                            val s = noiseFilter * (0.6 + 0.4 * sin(lfoPhase)) + sin(phase) * 0.05
                            s * vol * 20000.0
                        }
                        "celestial_drone" -> {
                            phase += 2.0 * PI * 432.0 / sampleRate
                            val s1 = sin(phase)
                            val s2 = sin(phase * 1.25)
                            val s3 = sin(phase * 1.5)
                            (s1 * 0.4 + s2 * 0.3 + s3 * 0.3) * vol * 20000.0
                        }
                        "delta_sleep" -> {
                            phase += 2.0 * PI * 108.0 / sampleRate
                            lfoPhase += 2.0 * PI * 2.5 / sampleRate
                            sin(phase) * (0.5 + 0.5 * sin(lfoPhase)) * vol * 22000.0
                        }
                        else -> 0.0
                    }
                    chunk[i] = sample.coerceIn(-32767.0, 32767.0).toInt().toShort()
                }
                track.write(chunk, 0, chunk.size)
            }
        }
    }

    fun stop() {
        synthesisJob?.cancel()
        synthesisJob = null
        currentSoundId = null
        runCatching {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.stop()
            audioTrack?.release()
        }
        audioTrack = null
    }

    fun isPlaying(soundId: String): Boolean = currentSoundId == soundId && synthesisJob?.isActive == true
}

@Composable
fun SoundscapesScreen(
    unlockedSoundscapes: Set<String> = emptySet(),
    onUnlockTrack: (String) -> Unit = {},
    onWatchHouseAd: () -> Unit = {},
    adsAllowed: Boolean = true,
    onBack: () -> Unit = {}
) {
    val ink = Color(0xFF17172A)
    val muted = Color(0xFF666477)
    val blush = Color(0xFFFFF0F3)
    val primaryRed = Color(0xFFE51D3E)
    val scope = rememberCoroutineScope()
    val audioEngine = remember { SoundscapeAudioEngine() }

    var activeTrackId by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var volume by remember { mutableFloatStateOf(0.75f) }
    var timerMinutes by remember { mutableIntStateOf(0) }
    var remainingSeconds by remember { mutableIntStateOf(0) }
    var pendingUnlockTrack by remember { mutableStateOf<SoundscapeItem?>(null) }

    DisposableEffect(Unit) {
        onDispose { audioEngine.stop() }
    }

    LaunchedEffect(volume) {
        audioEngine.setVolume(volume)
    }

    LaunchedEffect(timerMinutes, isPlaying) {
        if (isPlaying && timerMinutes > 0) {
            remainingSeconds = timerMinutes * 60
            while (remainingSeconds > 0 && isPlaying) {
                delay(1000)
                remainingSeconds--
            }
            if (remainingSeconds <= 0 && isPlaying) {
                audioEngine.stop()
                isPlaying = false
                activeTrackId = null
            }
        }
    }

    fun togglePlay(track: SoundscapeItem) {
        val isUnlocked = !track.isPremium || unlockedSoundscapes.contains(track.id)
        if (!isUnlocked) {
            pendingUnlockTrack = track
            return
        }

        if (activeTrackId == track.id && isPlaying) {
            audioEngine.stop()
            isPlaying = false
            activeTrackId = null
        } else {
            audioEngine.play(track.id, scope)
            activeTrackId = track.id
            isPlaying = true
        }
    }

    // Unlock Dialog
    if (pendingUnlockTrack != null) {
        val track = pendingUnlockTrack!!
        Dialog(onDismissRequest = { pendingUnlockTrack = null }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(track.emoji, fontSize = 42.sp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Unlock ${track.title}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ink,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        track.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = muted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        "Watch the optional BQ Sqwad AI video to unlock all four premium soundscapes. The tracks unlock only after the video finishes; skipping or closing does not grant the unlock.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ink,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = {
                            val id = track.id
                            pendingUnlockTrack = null
                            onUnlockTrack(id)
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryRed)
                    ) {
                        Text("▶  Watch Video to Unlock", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            pendingUnlockTrack = null
                            onWatchHouseAd()
                        },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("⭐  Watch BQ Sqwad AI video · unlock all 4 tracks")
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { pendingUnlockTrack = null }) {
                        Text("Maybe later", color = muted)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFAFAFD))
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Soothing Soundscapes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ink
                )
                Text(
                    "Calm your nervous system & rest deeply",
                    style = MaterialTheme.typography.bodySmall,
                    color = muted
                )
            }
            TextButton(onClick = onBack) {
                Text("Done", fontWeight = FontWeight.SemiBold, color = primaryRed)
            }
        }

        // Active Now Playing Bar
        if (activeTrackId != null && isPlaying) {
            val currentTrack = SOUNDSCAPE_CATALOG.firstOrNull { it.id == activeTrackId }
            if (currentTrack != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = blush),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(currentTrack.emoji, fontSize = 28.sp)
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        currentTrack.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = ink
                                    )
                                    Text(
                                        if (remainingSeconds > 0) "Timer: ${remainingSeconds / 60}m ${remainingSeconds % 60}s" else "Playing continuously",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = primaryRed
                                    )
                                }
                            }
                            IconButton(onClick = { togglePlay(currentTrack) }) {
                                Text("❚❚", fontSize = 22.sp, color = primaryRed)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔈", fontSize = 14.sp)
                            Slider(
                                value = volume,
                                onValueChange = { volume = it },
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = primaryRed,
                                    activeTrackColor = primaryRed
                                )
                            )
                            Text("🔊", fontSize = 14.sp)
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF))
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🎧", fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Sleep & Focus Timer",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = ink
                            )
                            Text(
                                "Set an automatic wind-down timer for bedtime",
                                style = MaterialTheme.typography.bodySmall,
                                color = muted
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(0 to "Off", 5 to "5m", 15 to "15m", 30 to "30m", 60 to "60m").forEach { (mins, label) ->
                            val selected = timerMinutes == mins
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selected) primaryRed else Color.White,
                                modifier = Modifier.weight(1f).clickable { timerMinutes = mins }
                            ) {
                                Text(
                                    label,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = TextAlign.Center,
                                    color = if (selected) Color.White else ink,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }

            // Soundscape Catalog Items
            items(SOUNDSCAPE_CATALOG) { track ->
                val isUnlocked = !track.isPremium || unlockedSoundscapes.contains(track.id)
                val isCurrentlyPlaying = activeTrackId == track.id && isPlaying

                Card(
                    onClick = { togglePlay(track) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrentlyPlaying) blush else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(if (isCurrentlyPlaying) 3.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp).clip(CircleShape).background(
                                if (isCurrentlyPlaying) primaryRed.copy(alpha = 0.15f) else Color(0xFFF3F3F7)
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(track.emoji, fontSize = 24.sp)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    track.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ink
                                )
                                if (track.isPremium && !isUnlocked) {
                                    Spacer(Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFFECC8)
                                    ) {
                                        Text(
                                            "PREMIUM",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF945200)
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(3.dp))
                            Text(
                                track.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = muted
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(
                                if (isCurrentlyPlaying) primaryRed else Color(0xFFEEEEF5)
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (isCurrentlyPlaying) "❚❚" else if (track.isPremium && !isUnlocked) "🔒" else "▶",
                                color = if (isCurrentlyPlaying) Color.White else ink,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // BQ Sqwad AI Companion Spotlight Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEFF))
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🤖", fontSize = 24.sp)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Want live companionship while resting?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF17172A)
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "BQ Sqwad AI Companion listens to you live with live voice chat and emotional support. Tap to watch the presentation or explore.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF3D3B52)
                        )
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = onWatchHouseAd,
                                modifier = Modifier.weight(1f).height(40.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Watch Video Presentation", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }

            // Preserved Bottom Banner Placement
            item {
                Spacer(Modifier.height(8.dp))
                BannerAdView(unitId = AdsConfig.BANNER_LIBRARY, enabled = adsAllowed)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
