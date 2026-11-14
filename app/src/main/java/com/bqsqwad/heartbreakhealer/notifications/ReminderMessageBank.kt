package com.bqsqwad.heartbreakhealer.notifications

import java.time.LocalDate

/**
 * A calm, non-pressuring daily rotation. Messages avoid assumptions about
 * relationships, identity, or how quickly someone should feel better.
 */
internal object ReminderMessageBank {
    private val messages = listOf(
        "A brand-new day is here. You do not have to solve everything today. Take one small step, check in with yourself, and give yourself credit for showing up.",
        "How are you doing today? Your check-in is a moment to notice what you need, not a test you have to pass. Open Heartbreak Healer whenever you feel ready.",
        "Your saved story is waiting whenever you want to return to it. A few pages can be a gentle break; there is no deadline and no pressure.",
        "Today can be simple: drink some water, take a comfortable breath, and choose one kind thing to do for yourself. Small moments count.",
        "A new day does not require a brand-new version of you. You are allowed to continue at your own pace. Your journal and check-in are here when useful.",
        "Have you explored the affirmations lately? Choose a message that feels helpful today, or skip it if it is not what you need.",
        "You have made it through days that once felt unfamiliar. Notice one thing you handled, however small, and let that be enough for today.",
        "How is your energy today? A quick check-in can help you name what is going well and what kind of support would feel useful.",
        "Your next story chapter can wait until you have time. Your reading progress is saved, so you can return without starting over.",
        "Try a small win today: tidy one corner, step outside for a little fresh air, or send a kind message to someone you trust.",
        "You do not need to compare your progress with anyone else's. Your path is allowed to look different, and taking a pause is okay.",
        "The journal is ready if writing would help you organise your thoughts. A sentence is enough; you do not have to write a perfect page.",
        "A gentle reminder: rest is allowed. You can use the app's evening wind-down when it suits you, or simply take a quiet moment away from your screen.",
        "What would make today feel a little easier? Your check-in can help you choose one realistic next step without judging how you feel.",
        "There is more to you than one difficult chapter. Give your attention to something you enjoy, even if it is only for a few minutes.",
        "If a story helped you unwind, your place is saved. Open the Stories library when you feel like reading again.",
        "You can revisit your coping cards whenever you need a practical reminder. Pick one idea and adapt it to what works for you.",
        "A new morning is an invitation, not an instruction. Move through today in a way that respects your time, energy, and boundaries.",
        "Check in with yourself today: what is one thing you need, and what is one thing you can leave for another day?",
        "Your memories and journal belong to you. You can use them to notice growth, or take a break from them whenever that feels better.",
        "Try the small-wins feature today. Progress is often made of ordinary choices that are easy to overlook.",
        "If today feels busy, keep it simple. One kind choice, one useful task, or one proper rest can be enough.",
        "You are allowed to change your mind about what support you need. Explore the app's tools and choose only what feels useful.",
        "Your story can continue when you are ready. Tap to open Heartbreak Healer and pick up from your saved place.",
        "How are you doing? You can record a quick check-in, write a note, or simply open the app and look around. There is no wrong way to use it.",
        "Take a moment to notice something steady around you: a familiar song, a comfortable place, or someone you can talk to. Let the day begin gently.",
        "The boundaries feature can help you think through what feels respectful and manageable. You can explore it without making any decisions today.",
        "A reminder for today: you deserve patience from yourself. You do not have to earn a break or prove that you are doing well.",
        "If you have a little time, explore one feature you have not tried yet. If not, that is completely fine too.",
        "Another day, another chance to care for yourself in a small way. Check in when you are ready, and let the rest wait."
    )

    fun today(): String {
        val day = LocalDate.now().toEpochDay()
        return messages[Math.floorMod(day, messages.size.toLong()).toInt()]
    }
}
