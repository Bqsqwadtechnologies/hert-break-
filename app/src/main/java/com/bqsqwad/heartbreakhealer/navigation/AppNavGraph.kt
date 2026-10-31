package com.bqsqwad.heartbreakhealer.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    NavHost(navController = navController, startDestination = Routes.ONBOARDING) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onComplete = { _, _, _ ->
                navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
            })
        }
        composable(Routes.DASHBOARD) {
            Column(Modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Healing Dashboard", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(24.dp))
                HeartMeter(progress = 0.35f, label = "Day 12")
                Spacer(Modifier.height(16.dp))
                Text("Current stage: Hurt", style = MaterialTheme.typography.titleMedium)
                Text("Hearts: 50", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.tertiary)
                Spacer(Modifier.height(32.dp))
                Button(onClick = { navController.navigate(Routes.SOS) }) { Text("Urge SOS") }
            }
        }
        composable(Routes.SOS) {
            // SOS placeholder until full screen is pushed
            Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Urge SOS", style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(16.dp))
                Text("You're stronger than this moment.")
                Spacer(Modifier.height(24.dp))
                Button(onClick = { navController.navigate(Routes.SUPPORT) }) { Text("Need more support?") }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { navController.popBackStack() }) { Text("I'm okay for now") }
            }
        }
        composable(Routes.SUPPORT) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Need more support?", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(16.dp))
                Text("This app is not therapy or a crisis service.")
                Spacer(Modifier.height(12.dp))
                Text("If you feel unsafe or at risk of harming yourself, contact local emergency services right away.")
                Spacer(Modifier.height(16.dp))
                Text("Find a helpline: findahelpline.com")
                Text("US: 988 Suicide & Crisis Lifeline")
                Spacer(Modifier.height(32.dp))
                Button(onClick = { navController.popBackStack() }) { Text("Back") }
            }
        }
        composable(Routes.JOURNAL) { Placeholder("Private Journal") }
        composable(Routes.NO_CONTACT) { Placeholder("No-Contact Tracker") }
        composable(Routes.REBUILD) { Placeholder("30-Day Rebuild Program") }
        composable(Routes.SELF_CARE) { Placeholder("Self-Care Planner") }
        composable(Routes.AFFIRMATIONS) { Placeholder("Daily Affirmations") }
        composable(Routes.SETTINGS) { Placeholder("Settings") }
        composable(Routes.MEMORIES) { Placeholder("Memories Box") }
        composable(Routes.HEARTS_SHOP) { Placeholder("Hearts Shop") }
    }
}

@Composable
private fun Placeholder(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
    }
}
