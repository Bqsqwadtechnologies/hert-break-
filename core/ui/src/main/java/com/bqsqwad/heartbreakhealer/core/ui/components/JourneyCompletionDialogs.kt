package com.bqsqwad.heartbreakhealer.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun JourneyRewardOfferDialog(
    title: String,
    description: String,
    showRewardedOption: Boolean,
    onWatchRewarded: () -> Unit,
    onContinue: () -> Unit
) {
    Dialog(onDismissRequest = onContinue) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("TODAY'S STEP IS SAVED", color = Color(0xFF2E7D32), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(description, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (showRewardedOption) {
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = onWatchRewarded, modifier = Modifier.fillMaxWidth()) {
                        Text("Watch a video • Earn 15 Hearts")
                    }
                    Spacer(Modifier.height(8.dp))
                } else {
                    Spacer(Modifier.height(16.dp))
                    Text("A video isn't available right now. Your progress is safe.", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                OutlinedButton(onClick = onContinue, modifier = Modifier.fillMaxWidth()) {
                    Text("Continue without watching")
                }
            }
        }
    }
}

@Composable
fun JourneySuccessDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 26.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GreenThumbUp(Modifier.size(104.dp))
                Spacer(Modifier.height(18.dp))
                Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(10.dp))
                Text(message, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(22.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("See you tomorrow")
                }
            }
        }
    }
}

@Composable
private fun GreenThumbUp(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val green = Color(0xFF218B52)
        drawCircle(color = Color(0xFFE5F5EB))
        val thumb = Path().apply {
            moveTo(size.width * 0.29f, size.height * 0.43f)
            lineTo(size.width * 0.40f, size.height * 0.43f)
            lineTo(size.width * 0.48f, size.height * 0.22f)
            quadraticTo(size.width * 0.51f, size.height * 0.13f, size.width * 0.59f, size.height * 0.17f)
            quadraticTo(size.width * 0.66f, size.height * 0.21f, size.width * 0.62f, size.height * 0.31f)
            lineTo(size.width * 0.59f, size.height * 0.41f)
            lineTo(size.width * 0.73f, size.height * 0.41f)
            quadraticTo(size.width * 0.82f, size.height * 0.41f, size.width * 0.80f, size.height * 0.51f)
            lineTo(size.width * 0.73f, size.height * 0.76f)
            quadraticTo(size.width * 0.71f, size.height * 0.83f, size.width * 0.63f, size.height * 0.83f)
            lineTo(size.width * 0.35f, size.height * 0.83f)
            quadraticTo(size.width * 0.29f, size.height * 0.83f, size.width * 0.29f, size.height * 0.76f)
            close()
        }
        drawPath(path = thumb, color = green, style = Fill)
        drawRoundRect(
            color = green,
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.18f, size.height * 0.43f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.15f, size.height * 0.40f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.035f)
        )
    }
}
