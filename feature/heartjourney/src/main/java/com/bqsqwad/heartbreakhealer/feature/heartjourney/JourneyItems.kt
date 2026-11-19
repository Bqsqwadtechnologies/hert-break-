package com.bqsqwad.heartbreakhealer.feature.heartjourney

import com.bqsqwad.heartbreakhealer.core.data.journey.JourneyEvents

/** route values must equal the constants in the app's `Routes` object. `free` = does not cost a Heart (like the SOS button on Home). */
data class JourneyItem(
    val id: String,
    val emoji: String,
    val title: String,
    val route: String?,
    val free: Boolean = false,
    val detect: Set<String>,
)

object JourneyItems {
    val all: List<JourneyItem> = listOf(
        JourneyItem("checkin", "💬", "Daily check-in", null, detect = setOf(JourneyEvents.CHECKIN)),
        JourneyItem("journal", "📓", "Journal", "journal", detect = setOf(JourneyEvents.JOURNAL)),
        JourneyItem("selfcare", "🌷", "Self-Care", "self_care", detect = setOf(JourneyEvents.SELFCARE)),
        JourneyItem("affirmations", "✨", "Affirmations", "affirmations", detect = setOf(JourneyEvents.visit("affirmations"))),
        JourneyItem("letter", "✉️", "Unsent letter", "unsent_letters", detect = setOf(JourneyEvents.LETTER)),
        JourneyItem("memories", "🖼️", "Memories", "memories", detect = setOf(JourneyEvents.MEMORY)),
        JourneyItem("triggers", "🧭", "Triggers", "triggers", detect = setOf(JourneyEvents.TRIGGER)),
        JourneyItem("nocontact", "🛡️", "No-Contact", "no_contact", detect = setOf(JourneyEvents.visit("no_contact"))),
        JourneyItem("coping", "🧰", "Coping Cards", "coping", detect = setOf(JourneyEvents.visit("coping"))),
        JourneyItem("boundaries", "🚧", "Boundary Scripts", "boundaries", detect = setOf(JourneyEvents.visit("boundaries"))),
        JourneyItem("capsule", "💌", "Letter to Future You", "capsule", detect = setOf(JourneyEvents.CAPSULE)),
        JourneyItem("wins", "🏅", "Small Wins", "wins", detect = setOf(JourneyEvents.visit("wins"))),
        JourneyItem("evening", "🌙", "Evening Wind-Down", "evening", detect = setOf(JourneyEvents.EVENING)),
        JourneyItem("grief", "💭", "Lonely or Missing Them?", "grief_contact", detect = setOf(JourneyEvents.visit("grief_contact"))),
        JourneyItem("library", "📚", "Healing Library", "library", detect = setOf(JourneyEvents.visit("library"))),
        JourneyItem("weekly", "📅", "Weekly Review", "weekly", detect = setOf(JourneyEvents.visit("weekly"))),
        JourneyItem("sos", "⚡", "Urge SOS", "sos", free = true, detect = setOf(JourneyEvents.URGE, JourneyEvents.visit("sos"))),
    )
}
