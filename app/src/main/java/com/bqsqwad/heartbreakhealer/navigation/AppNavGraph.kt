package com.bqsqwad.heartbreakhealer.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.bqsqwad.heartbreakhealer.feature.dashboard.DashboardViewModel
import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import com.bqsqwad.heartbreakhealer.core.common.ads.AdManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bqsqwad.heartbreakhealer.core.domain.model.HealingStage
import com.bqsqwad.heartbreakhealer.core.ui.components.DailyCheckIn
import com.bqsqwad.heartbreakhealer.core.ui.components.HeartMeter
import com.bqsqwad.heartbreakhealer.feature.affirmations.AffirmationsScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.BoundaryScriptsScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.CopingCardsScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.EveningWindDownScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.GriefVsContactScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.HealingLibraryScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.HealingPathScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.WeeklyReviewScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.SmallWinsScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.StageTrackerCard
import com.bqsqwad.heartbreakhealer.feature.dashboard.TimeCapsuleScreen
import com.bqsqwad.heartbreakhealer.feature.dashboard.UrgePassedCard
import com.bqsqwad.heartbreakhealer.feature.heartsshop.HeartsShopScreen
import com.bqsqwad.heartbreakhealer.feature.journal.JournalScreen
import com.bqsqwad.heartbreakhealer.feature.journal.JournalUiEntry
import com.bqsqwad.heartbreakhealer.feature.journal.UnsentLettersScreen
import com.bqsqwad.heartbreakhealer.feature.journal.LetterUi
import com.bqsqwad.heartbreakhealer.feature.memories.MemoriesScreen
import com.bqsqwad.heartbreakhealer.feature.memories.MemoryUi
import com.bqsqwad.heartbreakhealer.feature.settings.TriggerUi
import com.bqsqwad.heartbreakhealer.feature.nocontact.NoContactScreen
import com.bqsqwad.heartbreakhealer.feature.onboarding.OnboardingScreen
import com.bqsqwad.heartbreakhealer.feature.rebuild.RebuildScreen
import com.bqsqwad.heartbreakhealer.feature.selfcare.SelfCareScreen
import com.bqsqwad.heartbreakhealer.feature.settings.SettingsScreen
import com.bqsqwad.heartbreakhealer.feature.settings.TriggersScreen
import com.bqsqwad.heartbreakhealer.feature.sos.SosScreen
import java.time.LocalDate

object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val JOURNAL = "journal"
    const val LETTERS = "unsent_letters"
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
    const val COPING = "coping"
    const val BOUNDARIES = "boundaries"
    const val CAPSULE = "capsule"
    const val WINS = "wins"
    const val EVENING = "evening"
    const val GRIEF_CONTACT = "grief_contact"
    const val LIBRARY = "library"
    const val PATH = "path"
    const val WEEKLY = "weekly"
}

@Composable
fun AppNavGraph(navController: NavHostController, adManager: AdManager) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val onboardingComplete by dashboardViewModel.onboardingComplete.collectAsState()
    val capsuleText by dashboardViewModel.capsuleText.collectAsState()
    val capsuleUnlock by dashboardViewModel.capsuleUnlock.collectAsState()
    val hearts by dashboardViewModel.hearts.collectAsState()
    val urgePassedCount by dashboardViewModel.urgePassedCount.collectAsState()
    val journalEntries by dashboardViewModel.journalEntries.collectAsState()
    val unsentLetters by dashboardViewModel.unsentLetters.collectAsState()
    val bestNoContactDays by dashboardViewModel.bestNoContactDays.collectAsState()
    val noContactDays by dashboardViewModel.noContactDays.collectAsState()
    val selfCareCompleted by dashboardViewModel.selfCareCompleted.collectAsState()
    val rebuildProgress by dashboardViewModel.rebuildProgress.collectAsState()
    val reduceMotion by dashboardViewModel.reduceMotion.collectAsState()
    val remindersEnabled by dashboardViewModel.dailyRemindersEnabled.collectAsState()
    val savedMemories by dashboardViewModel.memories.collectAsState()
    val savedTriggers by dashboardViewModel.triggers.collectAsState()
    val activity = LocalContext.current as? Activity
    val adsAllowed by adManager.adsAllowed.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(onboardingComplete, backStackEntry?.destination?.route) {
        if (onboardingComplete && backStackEntry?.destination?.route == Routes.ONBOARDING) {
            navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
        }
    }
    NavHost(navController = navController, startDestination = Routes.ONBOARDING) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(onComplete = { nickname, breakupDate, goals ->
                dashboardViewModel.completeOnboarding(nickname, breakupDate, goals)
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                hearts = hearts,
                noContactDays = noContactDays,
                onSos = { navController.navigate(Routes.SOS) },
                onJournal = { navController.navigate(Routes.JOURNAL) },
                onLetters = { navController.navigate(Routes.LETTERS) },
                onNoContact = { navController.navigate(Routes.NO_CONTACT) },
                onSelfCare = { navController.navigate(Routes.SELF_CARE) },
                onRebuild = { navController.navigate(Routes.REBUILD) },
                onAffirmations = { navController.navigate(Routes.AFFIRMATIONS) },
                onHeartsShop = { navController.navigate(Routes.HEARTS_SHOP) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onMemories = { navController.navigate(Routes.MEMORIES) },
                onTriggers = { navController.navigate(Routes.TRIGGERS) },
                onCoping = { navController.navigate(Routes.COPING) },
                onLogUrge = dashboardViewModel::logUrgePassed,
                onBoundaries = { navController.navigate(Routes.BOUNDARIES) },
                onCapsule = { navController.navigate(Routes.CAPSULE) },
                onWins = { navController.navigate(Routes.WINS) },
                onEvening = { navController.navigate(Routes.EVENING) },
                onGriefContact = { navController.navigate(Routes.GRIEF_CONTACT) },
                onLibrary = { navController.navigate(Routes.LIBRARY) },
                onPath = { navController.navigate(Routes.PATH) },
                onWeekly = { navController.navigate(Routes.WEEKLY) },
                onCheckIn = dashboardViewModel::onCheckIn
            )
        }
        composable(Routes.SOS) {
            SosScreen(
                onNavigateToSupport = { navController.navigate(Routes.SUPPORT) },
                onUrgePassed = dashboardViewModel::logUrgePassed,
                onClose = { navController.popBackStack() }
            )
        }
        composable(Routes.JOURNAL) {
            JournalScreen(
                entries = journalEntries.map { JournalUiEntry(it.id, it.date, it.mood, it.content) },
                onSaveEntry = dashboardViewModel::saveJournalEntry,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.LETTERS) {
            UnsentLettersScreen(
                letters = unsentLetters.map { LetterUi(it.id, it.content, it.status) },
                onSave = dashboardViewModel::saveUnsentLetter,
                onRelease = dashboardViewModel::releaseUnsentLetter,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.NO_CONTACT) {
            NoContactScreen(
                currentDays = noContactDays,
                bestDays = bestNoContactDays,
                onSlip = dashboardViewModel::recordNoContactSlip,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SELF_CARE) {
            SelfCareScreen(
                completedTaskIds = selfCareCompleted.map { it.taskId }.toSet(),
                onToggle = { taskId -> dashboardViewModel.completeSelfCare(taskId); activity?.let { host -> adManager.showInterstitialIfAllowed(host) } },
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.REBUILD) {
            RebuildScreen(
                completedDays = rebuildProgress.filter { it.completed }.map { it.day }.toSet(),
                onCompleteDay = { day -> dashboardViewModel.completeRebuildDay(day); activity?.let { host -> adManager.showInterstitialIfAllowed(host) } },
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AFFIRMATIONS) { AffirmationsScreen(onBack = { navController.popBackStack() }, adsAllowed = adsAllowed) }
        composable(Routes.HEARTS_SHOP) {
            HeartsShopScreen(
                balance = hearts,
                onWatchRewarded = {
                    activity?.let { host ->
                        adManager.showRewarded(host, onRewarded = dashboardViewModel::onRewardedHearts)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToSupport = { navController.navigate(Routes.SUPPORT) },
                notificationsEnabled = remindersEnabled,
                reduceMotionEnabled = reduceMotion,
                onNotificationsChanged = { enabled ->
                    dashboardViewModel.setDailyRemindersEnabled(enabled)
                    activity?.let { host ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(host, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(host, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 7001)
                            }
                            com.bqsqwad.heartbreakhealer.notifications.NotificationScheduler.scheduleDaily(host)
                        } else {
                            com.bqsqwad.heartbreakhealer.notifications.NotificationScheduler.cancel(host)
                        }
                    }
                },
                onReduceMotionChanged = dashboardViewModel::setReduceMotion,
                onClearData = dashboardViewModel::clearAll,
                onBack = { navController.popBackStack()
                }
            )
        }
        composable(Routes.SUPPORT) { SupportScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.MEMORIES) {
            MemoriesScreen(
                memories = savedMemories.sorted().mapIndexed { index, item ->
                    val parts = item.split("\\t", limit = 2)
                    MemoryUi(index.toLong(), parts.firstOrNull().orEmpty(), parts.getOrElse(1) { "" })
                },
                onAdd = dashboardViewModel::addMemory,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TRIGGERS) {
            TriggersScreen(
                triggers = savedTriggers.sorted().mapIndexed { index, item ->
                    val parts = item.split("\\t", limit = 2)
                    TriggerUi(index.toLong(), parts.firstOrNull().orEmpty(), parts.getOrElse(1) { "" })
                },
                onAdd = dashboardViewModel::addTrigger,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.COPING) { CopingCardsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.BOUNDARIES) { BoundaryScriptsScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.CAPSULE) {
            TimeCapsuleScreen(
                existing = capsuleText,
                unlockDate = capsuleUnlock,
                onSave = dashboardViewModel::saveCapsule,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.WINS) {
            SmallWinsScreen(
                wins = buildList {
                    if (journalEntries.isNotEmpty()) add("Made time to check in with yourself")
                    if (noContactDays > 0) add("Built a no-contact streak of $noContactDays days")
                    if (hearts > 50) add("Earned hearts through healing activities")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.EVENING) {
            EveningWindDownScreen(
                onSave = dashboardViewModel::saveEveningReflection,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.GRIEF_CONTACT) { GriefVsContactScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.LIBRARY) { HealingLibraryScreen(onBack = { navController.popBackStack() }, adsAllowed = adsAllowed) }
        composable(Routes.PATH) { HealingPathScreen(onBack = { navController.popBackStack() }, adsAllowed = adsAllowed) }
        composable(Routes.WEEKLY) { WeeklyReviewScreen(checkIns = journalEntries.count { runCatching { java.time.LocalDate.parse(it.date).isAfter(java.time.LocalDate.now().minusDays(7)) }.getOrDefault(false) }, noContactDays = noContactDays, onBack = { navController.popBackStack() }, adsAllowed = adsAllowed) }
    }
}

@Composable
private fun DashboardScreen(
    hearts: Int,
    noContactDays: Int,
    onSos: () -> Unit,
    onJournal: () -> Unit,
    onLetters: () -> Unit,
    onNoContact: () -> Unit,
    onSelfCare: () -> Unit,
    onRebuild: () -> Unit,
    onAffirmations: () -> Unit,
    onHeartsShop: () -> Unit,
    onSettings: () -> Unit,
    onMemories: () -> Unit,
    onTriggers: () -> Unit,
    onCoping: () -> Unit,
    onLogUrge: () -> Unit,
    onBoundaries: () -> Unit,
    onCapsule: () -> Unit,
    onWins: () -> Unit,
    onEvening: () -> Unit,
    onGriefContact: () -> Unit,
    onLibrary: () -> Unit,
    onPath: () -> Unit,
    onWeekly: () -> Unit,
    onCheckIn: (Int, String) -> Unit
) {
    val ink = androidx.compose.ui.graphics.Color(0xFF17172A)
    val muted = androidx.compose.ui.graphics.Color(0xFF666477)
    val blush = androidx.compose.ui.graphics.Color(0xFFFFF0F3)
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    com.bqsqwad.heartbreakhealer.core.ui.components.HeartbreakHealerLogo(Modifier.size(46.dp))
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Heartbreak", style = MaterialTheme.typography.titleLarge, color = ink, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Text("HEALER", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, letterSpacing = 2.sp)
                        Text("$hearts hearts", style = MaterialTheme.typography.labelSmall, color = muted)
                    }
                }
                TextButton(onClick = onSettings) { Text("⚙ Settings", color = ink) }
            }
            Spacer(Modifier.height(18.dp))
            Card(colors = CardDefaults.cardColors(containerColor = blush), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Text("You are not alone ❤️", style = MaterialTheme.typography.titleLarge, color = ink, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("You are stronger than your pain. Take today one gentle step at a time.", color = muted)
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp)) {
                    Text("Your healing progress", style = MaterialTheme.typography.titleMedium, color = ink, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(92.dp)) {
                            CircularProgressIndicator(progress = { (noContactDays.coerceAtMost(30) / 30f) }, modifier = Modifier.fillMaxSize(), strokeWidth = 9.dp, color = MaterialTheme.colorScheme.primary, trackColor = blush)
                            Text("${noContactDays.coerceAtMost(30)}", color = ink, style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Keep going!", color = ink, style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            Text("Every small step counts.", color = muted)
                            Text("$noContactDays days logged", color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onSos, modifier = Modifier.fillMaxWidth().height(58.dp), shape = RoundedCornerShape(18.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE51D3E))) {
                Text("⚡  URGE SOS   •   Get help now", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
            Spacer(Modifier.height(18.dp))
            Text("Quick access", style = MaterialTheme.typography.titleLarge, color = ink, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            @Composable fun androidx.compose.foundation.layout.RowScope.FeatureTile(emoji: String, title: String, action: () -> Unit) {
                Card(onClick = action, modifier = Modifier.weight(1f), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = blush)) {
                    Column(Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(emoji, style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(5.dp))
                        Text(title, color = ink, style = MaterialTheme.typography.labelLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                FeatureTile("📓", "Journal", onJournal)
                FeatureTile("🌷", "Self-Care", onSelfCare)
                FeatureTile("🖼️", "Memories", onMemories)
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                FeatureTile("✨", "Affirmations", onAffirmations)
                FeatureTile("🧭", "Triggers", onTriggers)
                FeatureTile("🛍️", "Hearts Shop", onHeartsShop)
            }
            Spacer(Modifier.height(18.dp))
            Text("Your healing toolkit", style = MaterialTheme.typography.titleLarge, color = ink, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            listOf(
                "No-Contact" to onNoContact,
                "30-Day Rebuild" to onRebuild,
                "Coping Cards" to onCoping,
                "Boundary Scripts" to onBoundaries,
                "Letter to Future You" to onCapsule,
                "Small Wins" to onWins,
                "Evening Wind-Down" to onEvening,
                "Lonely or Missing Them?" to onGriefContact,
                "Healing Library" to onLibrary,
                "14-Day Healing Path" to onPath,
                "Weekly Review" to onWeekly
            ).forEach { (label, action) ->
                Card(onClick = action, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(15.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8FC))) {
                    Row(Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(label, color = ink, style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                        Text("›", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            UrgePassedCard(count = urgePassedCount, onLogged = onLogUrge)
            Spacer(Modifier.height(12.dp))
            DailyCheckIn(onSubmit = onCheckIn)
            Spacer(Modifier.height(18.dp))
            Text("A little support helps us keep this app free", color = muted, style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(6.dp))
            com.bqsqwad.heartbreakhealer.core.ui.ads.BannerAdView(
                unitId = com.bqsqwad.heartbreakhealer.core.common.ads.AdsConfig.BANNER_HOME,
                enabled = adsAllowed
            )
            Spacer(Modifier.height(10.dp))
        }
        NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
            listOf("Home" to "⌂", "Progress" to "◔", "Library" to "▤", "Shop" to "♡", "Settings" to "⚙").forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == 0,
                    onClick = when (index) {
                        3 -> onHeartsShop
                        4 -> onSettings
                        1 -> onWins
                        2 -> onAffirmations
                        else -> onSos
                    },
                    icon = { Text(item.second, style = MaterialTheme.typography.titleLarge) },
                    label = { Text(item.first) }
                )
            }
        }
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
