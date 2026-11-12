package com.bqsqwad.heartbreakhealer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.bqsqwad.heartbreakhealer.core.ui.theme.HeartbreakHealerTheme
import com.bqsqwad.heartbreakhealer.navigation.AppNavGraph
import com.bqsqwad.heartbreakhealer.ui.splash.AnimatedSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.bqsqwad.heartbreakhealer.core.common.ads.AdManager
import com.bqsqwad.heartbreakhealer.core.common.ads.ConsentHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var consentHelper: ConsentHelper
    @Inject lateinit var adManager: AdManager
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val pendingCrashReport = CrashReportStore.read(this)
        enableEdgeToEdge()
        consentHelper.requestConsent(this) { canShowAds ->
            adManager.onConsentResolved(this, canShowAds)
        }


        setContent {
            HeartbreakHealerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showAnimatedSplash by remember { mutableStateOf(true) }
                    var showCrashReport by remember { mutableStateOf(pendingCrashReport != null) }
                    if (showAnimatedSplash) {
                        AnimatedSplashScreen(
                            reduceMotion = false,
                            isFirstLaunch = true,
                            hapticsEnabled = true,
                            onFinished = {
                                showAnimatedSplash = false
                            }
                        )
                    } else {
                        HeartbreakHealerAppContent(adManager)
                    }
                    if (showCrashReport && pendingCrashReport != null) {
                        val clipboard = LocalClipboardManager.current
                        AlertDialog(
                            onDismissRequest = { },
                            title = { Text("The previous launch crashed") },
                            text = {
                                Column(Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState())) {
                                    Text("A diagnostic report was saved. Copy it and share it with support so the cause can be identified.")
                                    Spacer(Modifier.height(12.dp))
                                    Text(pendingCrashReport, style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { clipboard.setText(AnnotatedString(pendingCrashReport)) }) { Text("Copy report") }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    CrashReportStore.clear(this@MainActivity)
                                    showCrashReport = false
                                }) { Text("Close") }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeartbreakHealerAppContent(adManager: AdManager) {
    val navController = rememberNavController()
    AppNavGraph(navController = navController, adManager = adManager)
}
