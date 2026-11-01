package com.bqsqwad.heartbreakhealer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var keepSplash = true
        splashScreen.setKeepOnScreenCondition { keepSplash }

        setContent {
            HeartbreakHealerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showAnimatedSplash by remember { mutableStateOf(true) }
                    if (showAnimatedSplash) {
                        AnimatedSplashScreen(
                            reduceMotion = false,
                            isFirstLaunch = true,
                            hapticsEnabled = true,
                            onFinished = {
                                keepSplash = false
                                showAnimatedSplash = false
                            }
                        )
                    } else {
                        HeartbreakHealerAppContent()
                    }
                }
            }
        }
    }
}

@Composable
fun HeartbreakHealerAppContent() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}
