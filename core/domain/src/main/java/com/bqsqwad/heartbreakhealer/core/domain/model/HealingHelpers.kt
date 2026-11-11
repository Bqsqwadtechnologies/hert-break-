package com.bqsqwad.heartbreakhealer.core.domain.model

enum class HealingStage(val label: String, val hint: String) {
    SHOCK("Shock", "Things feel unreal. Be gentle with yourself."),
    HURT("Hurt", "The pain is real. You don't have to carry it alone."),
    ANGER("Anger", "Anger can protect you. Channel it into care for yourself."),
    ACCEPTANCE("Acceptance", "You're making space for what is. That takes courage."),
    GROWTH("Growth", "You're building a life that fits who you're becoming.")
}

object HealingStageResolver {
    /** Soft heuristic from days since breakup + check-in average — not a diagnosis. */
    fun resolve(daysSinceBreakup: Int, avgMood: Float?): HealingStage {
        return when {
            daysSinceBreakup < 7 -> HealingStage.SHOCK
            daysSinceBreakup < 21 -> HealingStage.HURT
            daysSinceBreakup < 45 -> if ((avgMood ?: 3f) < 2.5f) HealingStage.ANGER else HealingStage.HURT
            daysSinceBreakup < 90 -> HealingStage.ACCEPTANCE
            else -> HealingStage.GROWTH
        }
    }
}

data class CopingCard(val id: String, val title: String, val body: String)

object CopingDeck {
    val cards = listOf(
        CopingCard("outside", "Step outside", "Two minutes of sky or fresh air. That's enough."),
        CopingCard("water", "Drink water", "One glass. Your body is part of healing."),
        CopingCard("friend", "Message one safe person", "Not the ex. Someone who is kind to you."),
        CopingCard("room", "Name 3 things in the room", "Bring yourself back to right now."),
        CopingCard("breathe", "Four slow breaths", "In for 4, out for 6. Repeat."),
        CopingCard("music", "One song that holds you", "Not their playlist. Yours."),
        CopingCard("walk", "Short walk", "Around the block or down the hall."),
        CopingCard("write", "One honest sentence", "No one else has to read it."),
        CopingCard("cold", "Hold something cold", "Ice, a cold glass, cool water on wrists."),
        CopingCard("eat", "Eat something simple", "Toast, fruit, whatever you can manage."),
        CopingCard("stretch", "Stretch your body", "Shoulders, neck, unclench your jaw."),
        CopingCard("delay", "Delay the urge 10 minutes", "You can always decide again later."),
        CopingCard("photo", "Look at a photo of a good moment", "Not with them — of you, a place, a pet."),
        CopingCard("shower", "Warm or cool shower", "Reset the nervous system."),
        CopingCard("list", "List 3 things that are still true", "You are safe. This will pass. You are trying.")
    )
}

object BoundaryScripts {
    val scripts = listOf(
        "I need space right now. Please don't contact me." to "When they keep messaging",
        "I'm not ready to talk about us." to "When they want to 'just talk'",
        "Please respect that I'm taking time for myself." to "When mutual friends pressure you",
        "I won't be responding further. Take care." to "Final boundary after repeats",
        "I'm focusing on my healing. I can't help with that." to "When they ask for emotional labor",
        "This isn't a good time for me." to "Short and neutral"
    )
}
