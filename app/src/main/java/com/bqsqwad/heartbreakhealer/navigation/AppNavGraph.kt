package com.bqsqwad.heartbreakhealer.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bqsqwad.heartbreakhealer.core.ui.components.DailyCheckIn
import com.bqsqwad.heartbreakhealer.core.ui.components.HeartMeter
import com.bqsqwad.heartbreakhealer.feature.affirmations.AffirmationsScreen
import com.bqsqwad.heartbreakhealer.feature.heartsshop.HeartsShopScreen
import com.bqsqwad.heartbreakhealer.feature.journal.JournalScreen
import com.bqsqwad.heartbreakhealer.feature.memories.MemoriesScreen
import com.bqsqwad.heartbreakhealer.feature.nocontact.NoContactScreen
import com.bqsqwad.heartbreakhealer.feature.onboarding.OnboardingScreen
import com.bqsqwad.heartbreakhealer.feature.rebuild.RebuildScreen
import com.bqsqwad.heartbreakhealer.feature.selfcare.SelfCareScreen
import com.bqsqwad.heartbreakhealer.feature.settings.SettingsScreen
import com.bqsqwad.heartbreakhealer.feature.settings.TriggersScreen
import com.bqsqwad.heartbreakhealer.feature.sos.SosScreen

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
    const val HEARTS_SHOP = "hearts_shop"
    const val SUPPORT = "support"
    const val MEMORIES = "memories"
    const val TRIGGERS = "triggers"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.ONBOARDING) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onComplete = { _, _, _ ->
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onSos = { navController.navigate(Routes.SOS) },
                onJournal = { navController.navigate(Routes.JOURNAL) },
                onNoContact = { navController.navigate(Routes.NO_CONTACT) },
                onSelfCare = { navController.navigate(Routes.SELF_CARE) },
                onRebuild = { navController.navigate(Routes.REBUILD) },
                onAffirmations = { navController.navigate(Routes.AFFIRMATIONS) },
                onHeartsShop = { navController.navigate(Routes.HEARTS_SHOP) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onMemories = { navController.navigate(Routes.MEMORIES) },
                onTriggers = { navController.navigate(Routes.TRIGGERS) },
                onCheckIn = { _, _ -> /* wire to JournalRepository + HeartsRepository */ }
            )
        }
        composable(Routes.SOS) {
            SosScreen(
                onNavigateToSupport = { navController.navigate(Routes.SUPPORT) },
                onClose = { navController.popBackStack() }
            )
        }
        composable(Routes.JOURNAL) { JournalScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.NO_CONTACT) { NoContactScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.SELF_CARE) { SelfCareScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.REBUILD) { RebuildScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.AFFIRMATIONS) { AffirmationsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.HEARTS_SHOP) { HeartsShopScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToSupport = { navController.navigate(Routes.SUPPORT) },
                onClearData = { },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SUPPORT) { SupportScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.MEMORIES) { MemoriesScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.TRIGGERS) { TriggersScreen(onBack = { navController.popBackStack() }) }
    }
}

@Composable
private fun DashboardScreen(
    onSos: () -> Unit,
    onJournal: () -> Unit,
    onNoContact: () -> Unit,
    onSelfCare: () -> Unit,
    onRebuild: () -> Unit,
    onAffirmations: () -> Unit,
    onHeartsShop: () -> Unit,
    onSettings: () -> Unit,
    onMemories: () -> Unit,
    onTriggers: () -> Unit,
    onCheckIn: (Int, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Healing Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        HeartMeter(progress = 0.35f, label = "Day 12")
        Spacer(Modifier.height(8.dp))
        Text("Current stage: Hurt", style = MaterialTheme.typography.titleMedium)
        Text("Hearts: 50", color = MaterialTheme.colorScheme.tertiary)
        Spacer(Modifier.height(16.dp))
        DailyCheckIn(onSubmit = onCheckIn)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onSos, modifier = Modifier.fillMaxWidth()) { Text("Urge SOS") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onJournal, modifier = Modifier.fillMaxWidth()) { Text("Journal") }
        OutlinedButton(onClick = onNoContact, modifier = Modifier.fillMaxWidth()) { Text("No-Contact") }
        OutlinedButton(onClick = onSelfCare, modifier = Modifier.fillMaxWidth()) { Text("Self-Care") }
        OutlinedButton(onClick = onRebuild, modifier = Modifier.fillMaxWidth()) { Text("30-Day Rebuild") }
        OutlinedButton(onClick = onAffirmations, modifier = Modifier.fillMaxWidth()) { Text("Affirmations") }
        OutlinedButton(onClick = onHeartsShop, modifier = Modifier.fillMaxWidth()) { Text("Hearts Shop") }
        OutlinedButton(onClick = onMemories, modifier = Modifier.fillMaxWidth()) { Text("Memories Box") }
        OutlinedButton(onClick = onTriggers, modifier = Modifier.fillMaxWidth()) { Text("Triggers") }
        TextButton(onClick = onSettings) { Text("Settings") }
    }
}

@Composable
fun SupportScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Need more support?", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("This app is not therapy or a crisis service.")
        Spacer(Modifier.height(12.dp))
        Text("If you feel unsafe or at risk of harming yourself, contact local emergency services right away.")
        Spacer(Modifier.height(16.dp))
        Text("Find a helpline: findahelpline.com")
        Text("US: 988 Suicide & Crisis Lifeline")
        Spacer(Modifier.height(32.dp))
        Button(onClick = onBack) { Text("Back") }
    }
}
