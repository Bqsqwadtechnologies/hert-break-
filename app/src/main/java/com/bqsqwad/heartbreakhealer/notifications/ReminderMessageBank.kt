package com.bqsqwad.heartbreakhealer.notifications

import java.time.LocalDate

/**
 * Multi-period gentle notification message rotation covering daily reflection,
 * somatic breathwork, calming soundscapes, coping strategies, and soothing stories.
 */
internal object ReminderMessageBank {
    private val morningMessages = listOf(
        "A brand-new day is here. You do not have to solve everything today. Take one small step, check in with yourself, and give yourself credit for showing up.",
        "How are you doing today? Your check-in is a moment to notice what you need, not a test you have to pass. Open Heartbreak Healer whenever you feel ready.",
        "Today can be simple: drink some water, take a comfortable breath, and choose one kind thing to do for yourself. Small moments count.",
        "A new day does not require a brand-new version of you. You are allowed to continue at your own pace. Your journal and check-in are here when useful.",
        "Have you explored the affirmations lately? Choose a message that feels helpful today, or skip it if it is not what you need.",
        "You have made it through days that once felt unfamiliar. Notice one thing you handled, however small, and let that be enough for today.",
        "How is your energy today? A quick check-in can help you name what is going well and what kind of support would feel useful.",
        "Try a small win today: tidy one corner, step outside for a little fresh air, or send a kind message to someone you trust.",
        "You do not need to compare your progress with anyone else's. Your path is allowed to look different, and taking a pause is okay.",
        "The journal is ready if writing would help you organise your thoughts. A sentence is enough; you do not have to write a perfect page."
    )

    private val afternoonMessages = listOf(
        "Feeling tense or overwhelmed? Take 2 minutes for Somatic Breathwork. A gentle 4-7-8 reset calms your nervous system and drops anxiety.",
        "Pause and notice your shoulders. Let them drop away from your ears. Explore our Somatic Breathwork & Panic Reset whenever you need a breather.",
        "Midday reminder: Your heart is doing hard emotional lifting. Step away from your screens and let your breath anchor you in the present.",
        "Racing thoughts? Try the Physiological Sigh in the Somatic Breathwork toolkit: two deep breaths in, one long slow exhale.",
        "Take a moment to check your Coping Cards or read a quick comforting story. You are healing, even on days that feel slow.",
        "You are worthy of peace in the middle of a busy day. Tap to explore relaxing soundscapes and quick nervous system resets."
    )

    private val eveningMessages = listOf(
        "Nighttime can be tender. Open Soothing Soundscapes with 432Hz healing resonance and gentle rain to help hush racing thoughts.",
        "A gentle reminder: rest is allowed. Put on ocean tides or fireside embers in Soothing Soundscapes and drift off into restorative sleep.",
        "Close out today with the Evening Wind-Down reflection. What is one thing you can gently release before sleep?",
        "Your bed is a sanctuary. Put your phone on audio mode, start a calming soundscape timer, and let your body rest deeply.",
        "Whatever happened today is done. You handled it. Put on Delta Deep Sleep frequencies and let yourself recharge safely.",
        "Unsaved feelings can wait until tomorrow. Let our 432Hz harmonic tone wash over you as you settle in for the night."
    )

    fun today(): String {
        val day = LocalDate.now().toEpochDay()
        return morningMessages[Math.floorMod(day, morningMessages.size.toLong()).toInt()]
    }

    fun afternoon(): String {
        val day = LocalDate.now().toEpochDay()
        return afternoonMessages[Math.floorMod(day, afternoonMessages.size.toLong()).toInt()]
    }

    fun evening(): String {
        val day = LocalDate.now().toEpochDay()
        return eveningMessages[Math.floorMod(day, eveningMessages.size.toLong()).toInt()]
    }
}
