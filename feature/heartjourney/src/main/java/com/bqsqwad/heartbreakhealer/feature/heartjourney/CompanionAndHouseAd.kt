package com.bqsqwad.heartbreakhealer.feature.heartjourney

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

/** Opens BQ Sqwad AI: installed app first, then the store listing, then the website. Links come from config/companion.properties. */
object CompanionLinks {
    fun openSignup(context: Context) {
        val signupUrl = BuildConfig.COMPANION_WEB_URL.trimEnd('/') + "/signup"
        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(signupUrl)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun open(context: Context) {
        val pkg = BuildConfig.COMPANION_PACKAGE
        val installed = if (pkg.isNotBlank()) context.packageManager.getLaunchIntentForPackage(pkg) else null
        val intent = installed ?: Intent(
            Intent.ACTION_VIEW,
            Uri.parse(BuildConfig.COMPANION_STORE_URL.ifBlank { BuildConfig.COMPANION_WEB_URL }),
        )
        runCatching { context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
    }
}

/** The explanation card with the three buttons the owner asked for. */
@Composable
fun CompanionCard(onProceed: () -> Unit, onNoThanks: () -> Unit, onUsingAlready: () -> Unit, onWatchPromo: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEFF)),
    ) {
        Column(Modifier.padding(18.dp)) {
            Text("Want to talk it out live?", style = MaterialTheme.typography.titleMedium, color = Color(0xFF17172A), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "BQ Sqwad AI Companion can listen to you live. It has live voice chat, a live AI camera, a learning companion and Brain Guard.",
                color = Color(0xFF3D3B52), style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Open BQ Sqwad AI, tap the + inside the chat box (top right) or More features, then choose Companion. It will be there listening to you live.",
                color = Color(0xFF3D3B52), style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Companion is not therapy or a crisis service. If you feel unsafe, open Support in this app or contact local emergency services.",
                color = Color(0xFF666477), style = MaterialTheme.typography.labelSmall,
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = onProceed, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Proceed with Companion")
            }
            if (onWatchPromo != null) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onWatchPromo,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text("Watch BQ Sqwad AI video · earn 15 Hearts")
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onNoThanks, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(14.dp)) { Text("No thanks") }
                OutlinedButton(onClick = onUsingAlready, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(14.dp)) { Text("I'm using it already") }
            }
        }
    }
}

/** Declining or already using the companion simply dismisses the optional promotion. */
@Composable
fun CompanionPromptDialog(onClose: () -> Unit, onPromoVideoFinished: (() -> Unit)? = null) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onClose) {
        CompanionCard(
            onProceed = { CompanionLinks.open(context); onClose() },
            onNoThanks = onClose,
            onUsingAlready = onClose,
        )
    }
}

private const val SKIP_AFTER_SECONDS = 30

/**
 * The BQ Sqwad AI house video.
 *  - The video itself never redirects; only the clearly labeled Learn more action opens the companion.
 *  - A reward becomes eligible after 30 seconds of active playback; skipping afterwards keeps it.
 *  - No countdown is shown. Skip appears after 30 seconds. Pause and Mute are real controls.
 *  - Labeled "Ad". Never shown on SOS, Support, Journal or Letters screens.
 */
@Composable
fun HouseVideoAd(onRewardEligible: () -> Unit = {}, onClose: () -> Unit) {
    val context = LocalContext.current
    val resId = remember { context.resources.getIdentifier("bqsqwad_ai_promo", "raw", context.packageName) }
    if (resId == 0) {
        // Without an actual playable video, do not grant a completion reward.
        Dialog(onDismissRequest = onClose) {
            PromoEndCard(onOpen = { CompanionLinks.open(context); onClose() }, onClose = onClose)
        }
        return
    }
    var videoView by remember { mutableStateOf<VideoView?>(null) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var playing by remember { mutableStateOf(true) }
    var muted by remember { mutableStateOf(false) }
    var ended by remember { mutableStateOf(false) }
    var rewardReported by remember { mutableStateOf(false) }
    var seconds by remember { mutableIntStateOf(0) }
    var playbackStarted by remember { mutableStateOf(false) }

    LaunchedEffect(playing, ended, playbackStarted) {
        while (playing && !ended && playbackStarted) {
            delay(1000)
            val nextSeconds = seconds + 1
            seconds = nextSeconds
            if (nextSeconds >= SKIP_AFTER_SECONDS && !rewardReported) {
                rewardReported = true
                onRewardEligible()
            }
        }
    }
    DisposableEffect(Unit) { onDispose { runCatching { videoView?.stopPlayback() } } }
    BackHandler { onClose() }

    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false)) {
        Box(Modifier.fillMaxSize().background(Color.Black)) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoURI(Uri.parse("android.resource://${ctx.packageName}/$resId"))
                        setOnPreparedListener { mp -> player = mp; mp.isLooping = false; playbackStarted = true; start() }
                        setOnCompletionListener {
                            ended = true
                            playing = false
                            // Covers a video that reaches its end on the 30-second boundary
                            // before the one-second ticker gets its next turn.
                            if (seconds >= SKIP_AFTER_SECONDS && !rewardReported) {
                                rewardReported = true
                                onRewardEligible()
                            }
                        }
                        videoView = this
                    }
                },
            )
            Surface(
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
                shape = RoundedCornerShape(8.dp), color = Color(0x99000000),
            ) { Text("Ad · BQ Sqwad AI", color = Color.White, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) }

            // The countdown stays hidden. The skip action appears only after 30 seconds.
            val arrowPulse = rememberInfiniteTransition(label = "skip-arrow")
                .animateFloat(
                    initialValue = 0.25f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 550),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "skip-arrow-alpha"
                )
            Column(
                modifier = Modifier.align(Alignment.TopEnd).padding(14.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (seconds >= SKIP_AFTER_SECONDS || ended) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (!ended) {
                            Text(
                                "➜",
                                color = Color(0xFFFFD54F),
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.alpha(arrowPulse.value)
                            )
                        }
                        Surface(shape = RoundedCornerShape(12.dp), color = Color(0xDD000000)) {
                            TextButton(
                                onClick = onClose,
                                modifier = Modifier.height(44.dp).padding(horizontal = 6.dp)
                            ) {
                                Text(if (ended) "Close ✕" else "Skip ›", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = { CompanionLinks.openSignup(context) },
                        modifier = Modifier.height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Sign up for BQ Sqwad AI", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Button(
                onClick = { CompanionLinks.open(context) },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 118.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Text("Learn more about BQ Sqwad AI")
            }

            // Bottom controls: Pause / Play, Mute / Unmute
            Row(
                Modifier.align(Alignment.BottomCenter).padding(bottom = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp),
            ) {
                RoundControl(if (playing) "❚❚" else "▶", if (playing) "Pause video" else "Play video") {
                    val v = videoView ?: return@RoundControl
                    if (playing) {
                        v.pause()
                        playing = false
                    } else {
                        if (ended) {
                            ended = false
                            seconds = 0
                            rewardReported = false
                            playbackStarted = true
                            v.seekTo(0)
                        }
                        v.start()
                        playing = true
                    }
                }
                RoundControl(if (muted) "🔇" else "🔊", if (muted) "Unmute video" else "Mute video") {
                    muted = !muted
                    player?.let { mp ->
                        val vol = if (muted) 0f else 1f
                        mp.setVolume(vol, vol)
                    }
                }
            }
            if (ended) {
                Column(
                    Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Button(onClick = { CompanionLinks.open(context) }, shape = RoundedCornerShape(16.dp)) { Text("Learn more about BQ Sqwad AI") }
                }
            }
        }
    }
}

@Composable
private fun RoundControl(label: String, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(56.dp).background(Color(0xAA000000), CircleShape)
            .clickable(onClick = onClick).semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) { Text(label, color = Color.White, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center) }
}

@Composable
private fun PromoEndCard(onOpen: () -> Unit, onClose: () -> Unit) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Ad · BQ Sqwad AI", style = MaterialTheme.typography.labelMedium, color = Color(0xFF666477))
            Spacer(Modifier.height(8.dp))
            Text("Meet BQ Sqwad AI", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF17172A))
            Spacer(Modifier.height(6.dp))
            Text("Live voice chat, a live AI camera and a learning companion.", textAlign = TextAlign.Center, color = Color(0xFF3D3B52))
            Spacer(Modifier.height(16.dp))
            Button(onClick = onOpen, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) { Text("Learn more") }
            TextButton(onClick = onClose) { Text("Close") }
        }
    }
}
