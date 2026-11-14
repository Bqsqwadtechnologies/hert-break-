package com.bqsqwad.heartbreakhealer.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.bqsqwad.heartbreakhealer.feature.dashboard.DashboardViewModel
import android.app.Activity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import com.bqsqwad.heartbreakhealer.core.common.ads.AdManager
import com.bqsqwad.heartbreakhealer.core.common.ads.InterstitialPlacement
import com.bqsqwad.heartbreakhealer.core.common.ads.RewardedPlacement
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
import com.bqsqwad.heartbreakhealer.feature.entertainment.EntertainmentScreen
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
    const val ENTERTAINMENT = "entertainment"
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
    val completedPathDays by dashboardViewModel.completedPathDays.collectAsState()
    val rebuildCompletedDays by dashboardViewModel.rebuildCompletedDays.collectAsState()
    val lastJourneyCompletionDate by dashboardViewModel.lastJourneyCompletionDate.collectAsState()
    val viewedStoryIds by dashboardViewModel.viewedStoryIds.collectAsState()
    val seenJokeIds by dashboardViewModel.seenJokeIds.collectAsState()
    // Both guided journeys share a single daily completion lock in DataStore.
    val context = LocalContext.current
    val activity = context as? Activity
    val adsAllowed by adManager.adsAllowed.collectAsState()
    var pendingFeatureRoute by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    var pendingRewardGranted by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var pendingRewardDismissed by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var pendingRewardCredited by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val openFeature: (String) -> Unit = { route ->
        dashboardViewModel.spendHearts(1) { paid ->
            if (paid) navController.navigate(route)
            else {
                pendingFeatureRoute = route
                navController.navigate(Routes.HEARTS_SHOP)
            }
        }
    }
    fun finishPendingReward() {
        if (pendingRewardGranted && pendingRewardDismissed && pendingRewardCredited) {
            pendingRewardGranted = false
            pendingRewardDismissed = false
            pendingRewardCredited = false
            val target = pendingFeatureRoute
            pendingFeatureRoute = null
            if (target != null) {
                dashboardViewModel.spendHearts(1) { paid ->
                    if (paid) navController.navigate(target) {
                        popUpTo(Routes.HEARTS_SHOP) { inclusive = true }
                    }
                    else activity?.let { host ->
                        Toast.makeText(host, "Please earn more Hearts to open this feature.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    val backStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(onboardingComplete, backStackEntry?.destination?.route) {
        if (onboardingComplete && backStackEntry?.destination?.route == Routes.ONBOARDING) {
            navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
        }
    }
    LaunchedEffect(backStackEntry?.destination?.route) {
        val route = backStackEntry?.destination?.route
        adManager.setAppOpenEligible(
            route in setOf(
                Routes.DASHBOARD,
                Routes.SELF_CARE,
                Routes.REBUILD,
                Routes.AFFIRMATIONS,
                Routes.HEARTS_SHOP,
                Routes.LIBRARY,
                Routes.PATH,
                Routes.WEEKLY,
                Routes.ENTERTAINMENT
            )
        )
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
                urgePassedCount = urgePassedCount,
                adsAllowed = adsAllowed,
                onSos = { navController.navigate(Routes.SOS) },
                onJournal = { openFeature(Routes.JOURNAL) },
                onLetters = { openFeature(Routes.LETTERS) },
                onNoContact = { openFeature(Routes.NO_CONTACT) },
                onSelfCare = { openFeature(Routes.SELF_CARE) },
                onRebuild = { openFeature(Routes.REBUILD) },
                onAffirmations = { openFeature(Routes.AFFIRMATIONS) },
                onHeartsShop = { pendingFeatureRoute = null; navController.navigate(Routes.HEARTS_SHOP) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onMemories = { openFeature(Routes.MEMORIES) },
                onTriggers = { openFeature(Routes.TRIGGERS) },
                onCoping = { openFeature(Routes.COPING) },
                onLogUrge = dashboardViewModel::logUrgePassed,
                onBoundaries = { openFeature(Routes.BOUNDARIES) },
                onCapsule = { openFeature(Routes.CAPSULE) },
                onWins = { openFeature(Routes.WINS) },
                onEvening = { openFeature(Routes.EVENING) },
                onGriefContact = { openFeature(Routes.GRIEF_CONTACT) },
                onLibrary = { openFeature(Routes.LIBRARY) },
                onPath = { openFeature(Routes.PATH) },
                onWeekly = { openFeature(Routes.WEEKLY) },
                onEntertainment = { openFeature(Routes.ENTERTAINMENT) },
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
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.LETTERS) {
            UnsentLettersScreen(
                letters = unsentLetters.map { LetterUi(it.id, it.content, it.status) },
                onSave = dashboardViewModel::saveUnsentLetter,
                onRelease = dashboardViewModel::releaseUnsentLetter,
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.NO_CONTACT) {
            NoContactScreen(
                currentDays = noContactDays,
                bestDays = bestNoContactDays,
                onSlip = dashboardViewModel::recordNoContactSlip,
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SELF_CARE) {
            SelfCareScreen(
                completedTaskIds = selfCareCompleted.map { it.taskId }.toSet(),
                onToggle = { taskId -> dashboardViewModel.completeSelfCare(taskId); activity?.let { host -> adManager.showInterstitialIfAllowed(host, InterstitialPlacement.SELF_CARE_COMPLETION) } },
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.REBUILD) {
            val completedDays = rebuildProgress.filter { it.completed }.map { it.day }.toSet() +
                rebuildCompletedDays.mapNotNull { it.toIntOrNull() }
            RebuildScreen(
                completedDays = completedDays,
                completedToday = lastJourneyCompletionDate == LocalDate.now().toString(),
                onCompleteDay = { day, onResult ->
                    dashboardViewModel.completeJourneyDay(
                        journey = com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences.JOURNEY_REBUILD,
                        day = day,
                        alreadyCompletedDays = completedDays,
                        onResult = onResult
                    )
                },
                onWatchRewarded = { onFinished ->
                    val host = activity
                    if (host == null) {
                        onFinished()
                    } else {
                        adManager.showRewarded(
                            host,
                            RewardedPlacement.REBUILD_30,
                            onRewarded = { dashboardViewModel.onRewardedHearts() },
                            onDismissed = onFinished,
                            onUnavailable = { onFinished() }
                        )
                    }
                },
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AFFIRMATIONS) { AffirmationsScreen(onBack = { navController.popBackStack() }, adsAllowed = adsAllowed) }
        composable(Routes.HEARTS_SHOP) {
            HeartsShopScreen(
                balance = hearts,
                onWatchRewarded = {
                    pendingRewardGranted = false
                    pendingRewardDismissed = false
                    pendingRewardCredited = false
                    activity?.let { host ->
                        adManager.showRewarded(
                            host,
                            RewardedPlacement.HEARTS_REWARD,
                            onRewarded = {
                                pendingRewardGranted = true
                                dashboardViewModel.onRewardedHearts {
                                    pendingRewardCredited = true
                                    finishPendingReward()
                                }
                            },
                            onDismissed = {
                                pendingRewardDismissed = true
                                finishPendingReward()
                            },
                            onUnavailable = {
                                Toast.makeText(host, "A rewarded video is not ready yet. Please try again shortly.", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                onWatchExtraRewarded = {
                    pendingRewardGranted = false
                    pendingRewardDismissed = false
                    pendingRewardCredited = false
                    activity?.let { host ->
                        adManager.showRewarded(
                            host,
                            RewardedPlacement.SHOP_EXTRA_REWARD,
                            onRewarded = {
                                pendingRewardGranted = true
                                dashboardViewModel.onRewardedHearts {
                                    pendingRewardCredited = true
                                    finishPendingReward()
                                }
                            },
                            onDismissed = {
                                pendingRewardDismissed = true
                                finishPendingReward()
                            },
                            onUnavailable = {
                                Toast.makeText(host, "A rewarded video is not ready yet. Please try again shortly.", Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                onBack = { pendingFeatureRoute = null; navController.popBackStack() },
                adsAllowed = adsAllowed
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
                adsAllowed = adsAllowed,
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
                adsAllowed = adsAllowed,
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
                adsAllowed = adsAllowed,
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
        composable(Routes.PATH) {
            val completedDays = completedPathDays.mapNotNull { it.toIntOrNull() }.toSet()
            HealingPathScreen(
                days = (1..14).map {
                    com.bqsqwad.heartbreakhealer.feature.dashboard.PathDay(
                        it,
                        "Day $it — one small act of care",
                        it in completedDays
                    )
                },
                completedToday = lastJourneyCompletionDate == LocalDate.now().toString(),
                onCompleteDay = { day, onResult ->
                    dashboardViewModel.completeJourneyDay(
                        journey = com.bqsqwad.heartbreakhealer.core.data.prefs.UserPreferences.JOURNEY_HEALING_PATH,
                        day = day,
                        alreadyCompletedDays = completedDays,
                        onResult = onResult
                    )
                },
                onWatchRewarded = { onFinished ->
                    val host = activity
                    if (host == null) {
                        onFinished()
                    } else {
                        adManager.showRewarded(
                            host,
                            RewardedPlacement.HEALING_PATH_14,
                            onRewarded = { dashboardViewModel.onRewardedHearts() },
                            onDismissed = onFinished,
                            onUnavailable = {
                                onFinished()
                            }
                        )
                    }
                },
                onBack = { navController.popBackStack() },
                adsAllowed = adsAllowed
            )
        }
        composable(Routes.WEEKLY) { WeeklyReviewScreen(checkIns = journalEntries.count { runCatching { java.time.LocalDate.parse(it.date).isAfter(java.time.LocalDate.now().minusDays(7)) }.getOrDefault(false) }, urgesRodeOut = urgePassedCount, noContactDays = noContactDays, onBack = { if (activity != null) adManager.showInterstitialIfAllowed(activity, InterstitialPlacement.WEEKLY_REVIEW_EXIT) { navController.popBackStack() } else navController.popBackStack() }, adsAllowed = adsAllowed) }
        composable(Routes.ENTERTAINMENT) {
            EntertainmentScreen(
                viewedStoryIds = viewedStoryIds,
                seenJokeIds = seenJokeIds,
                onMarkStoryViewed = dashboardViewModel::markStoryViewed,
                onMarkJokeSeen = dashboardViewModel::markJokeSeen,
                onUnlock = { kind, onUnlocked ->
                    val host = activity
                    if (host == null) {
                        onUnlocked()
                    } else {
                        val placement = if (kind == "joke") RewardedPlacement.JOKE_UNLOCK else RewardedPlacement.STORY_UNLOCK
                        adManager.showRewarded(
                            host,
                            placement,
                            onRewarded = onUnlocked,
                            onDismissed = {},
                            onUnavailable = onUnlocked
                        )
                    }
                },
                adsAllowed = adsAllowed,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
private fun DashboardScreen(
    hearts: Int,
    noContactDays: Int,
    urgePassedCount: Int,
    adsAllowed: Boolean,
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
    onEntertainment: () -> Unit,
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
            Text("Optional tools use 1 Heart per visit. Urge SOS, support and Settings are always free.", style = MaterialTheme.typography.bodySmall, color = muted)
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
                "Weekly Review" to onWeekly,
                "Stories & Jokes" to onEntertainment
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
                        1 -> onWeekly
                        2 -> onLibrary
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
