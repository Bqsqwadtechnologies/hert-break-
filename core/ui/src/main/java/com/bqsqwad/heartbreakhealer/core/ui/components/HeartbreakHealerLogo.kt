package com.bqsqwad.heartbreakhealer.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.bqsqwad.heartbreakhealer.core.ui.R

/**
 * Displays the repository's single official logo source:
 * branding/logo/logo.png. Splash animation and all callers keep their existing layout.
 */
@Composable
fun HeartbreakHealerLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.official_healed_heart),
        contentDescription = "Heartbreak Healer logo",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}
