package com.bqsqwad.heartbreakhealer.feature.affirmations

object AffirmationBank {
    val all = listOf(
        "I deserve peace.",
        "This feeling will pass.",
        "I am rebuilding a life I love.",
        "My worth is not defined by someone else.",
        "I choose kindness toward myself today.",
        "Healing is not linear, and that is okay.",
        "I am allowed to take up space.",
        "Every day I grow a little stronger.",
        "I release what no longer serves me.",
        "I am enough as I am.",
        "My heart is learning to trust again.",
        "I protect my energy.",
        "I can sit with discomfort without acting on it.",
        "I am becoming someone I am proud of.",
        "Softness is strength.",
        "I do not need to rush my healing.",
        "I am free to write a new story.",
        "My boundaries are an act of love.",
        "I am not behind; I am on my own path.",
        "Today I choose myself.",
        "I can miss someone and still move forward.",
        "My future is open.",
        "I am safe in this moment.",
        "I honor my feelings without being ruled by them.",
        "I am worthy of steady love.",
        "Rest is productive.",
        "I forgive myself for staying too long.",
        "I am rebuilding trust with myself.",
        "Small steps still count.",
        "I am allowed to change.",
        "My peace matters more than proving a point.",
        "I can start again as many times as I need.",
        "I am not broken; I am healing.",
        "I give myself permission to feel.",
        "I am the author of what comes next.",
        "Clarity grows when I am still.",
        "I choose progress over perfection.",
        "My life is not on hold.",
        "I am learning to stay when it is hard.",
        "Hope is a practice."
    )

    fun today(): String {
        val day = java.time.LocalDate.now().toEpochDay().toInt()
        return all[day.mod(all.size)]
    }
}
