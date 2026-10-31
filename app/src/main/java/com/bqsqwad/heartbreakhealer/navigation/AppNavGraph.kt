package com.bqsqwad.heartbreakhealer.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bqsqwad.heartbreakhealer.core.ui.components.HeartMeter
import com.bqsqwad.heartbreakhealer.feature.onboarding.OnboardingScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val JOURNAL = "journal"
    const val SOS = "sos"
    const val NO_CONTACT = "no_contact"
    const val REBUILD = "rebuild"
    const val SELF_CARE = "self_care"
    const val AFFIRMATIONS = "affirmations"
    const val SETTINGS = "settings"
    const val MEMORIES = "memories"
    const val HEARTS_SHOP = "hearts_shop"
    const val SUPPORT = "support"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = { _, _, _ ->
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardPlaceholder()
        }
        composable(Routes.JOURNAL) { PlaceholderScreen("Private Journal") }
        composable(Routes.SOS) { PlaceholderScreen("Urge SOS") }
        composable(Routes.NO_CONTACT) { PlaceholderScreen("No-Contact Tracker") }
        composable(Routes.REBUILD) { PlaceholderScreen("30-Day Rebuild Program") }
        composable(Routes.SELF_CARE) { PlaceholderScreen("Self-Care Planner") }
        composable(Routes.AFFIRMATIONS) { PlaceholderScreen("Daily Affirmations") }
        composable(Routes.SETTINGS) { PlaceholderScreen("Settings") }
        composable(Routes.MEMORIES) { PlaceholderScreen("Memories Box") }
        composable(Routes.HEARTS_SHOP) { PlaceholderScreen("Hearts Shop") }
        composable(Routes.SUPPORT) { PlaceholderScreen("Need more support?") }
    }
}

@Composable
private fun DashboardPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Healing Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(24.dp))
        HeartMeter(
            progress = 0.35f,
            label = "Day 12",
            reduceMotion = false
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Current stage: Hurt",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Hearts: 50",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
