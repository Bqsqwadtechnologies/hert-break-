package com.bqsqwad.heartbreakhealer.feature.heartjourney

/** Gentle, original, never guilt-based. Rotation guarantees a different message each day. */
object JourneyMessages {
    /** Index = day number - 1. Shown in the daily notification and the welcome. */
    val daily: List<String> = listOf(
        "Day 1 is yours. Showing up already took courage, and I'm with you.",
        "Day 2. You came back. That matters more than how you feel today.",
        "Day 3. Small steps still move you forward. Take one today.",
        "Day 4. You do not have to be okay all day. Be kind to yourself for a few minutes.",
        "Day 5. Healing is not a straight line, and you are still on it.",
        "Day 6. Drink some water, breathe slowly, and read a story today.",
        "Day 7. One whole week. Look how far you have already come.",
        "Day 8. Your heart is learning a new rhythm. Give it time.",
        "Day 9. You are allowed to laugh today. A joke can be medicine too.",
        "Day 10. A third of the way there. Be proud of that.",
        "Day 11. You are more than one chapter of your life.",
        "Day 12. Rest if you need to, then take one small step.",
        "Day 13. Notice one thing today that is going right.",
        "Day 14. Two weeks of showing up for yourself. That is real strength.",
        "Day 15. Halfway. Your heart is closing a little more every day.",
        "Day 16. Missing someone and moving forward can happen on the same day.",
        "Day 17. You deserve patience, especially from yourself.",
        "Day 18. Choose one thing today that is only for you.",
        "Day 19. You are not alone, and you never have to rush.",
        "Day 20. Two thirds done. The hardest part is behind you.",
        "Day 21. Three weeks. Habits of care are forming.",
        "Day 22. You are building something new, one day at a time.",
        "Day 23. Look back at Day 1. You have grown.",
        "Day 24. Peace is starting to feel familiar.",
        "Day 25. Five days to go. Keep going gently.",
        "Day 26. You keep showing up for yourself, and it shows.",
        "Day 27. Almost there. Your heart is nearly whole.",
        "Day 28. Three days left. Be proud of every day you counted.",
        "Day 29. Tomorrow could be your last step. Rest well tonight.",
        "Day 30. This is it. Today your heart finishes healing.",
    )

    private val afterTask: List<String> = listOf(
        "Nice work. One more step toward feeling like yourself.",
        "That counts. Every small thing you do for yourself adds up.",
        "Well done. Take a slow breath and let that sink in.",
        "You did that. Remember this feeling tomorrow.",
        "Good. Your heart is a little stronger than it was this morning.",
        "That was kind to yourself. Keep going at your own pace.",
        "Another step done. You are doing better than you think.",
        "Proud of you for showing up today.",
        "One more piece put back in place.",
        "You chose healing today. That is not small.",
        "Lovely. The day already looks a little brighter.",
        "Look at you, keeping the promise you made to yourself.",
        "That took effort, and you made it. Well done.",
        "A calm moment earned. Enjoy it.",
        "You are rebuilding, quietly and steadily.",
        "That is how healing happens: one honest step at a time.",
        "Great job. Your future self says thank you.",
        "You are not stuck. You are moving, and it shows.",
        "Done. Be gentle with yourself for the rest of the day.",
        "Every task you finish is a vote for the life you want.",
        "Good step. Remember that rest counts too.",
        "You showed up again. That is real strength.",
        "That was a brave little step. Well done.",
        "Your heart is listening, and it is getting stronger.",
        "Beautiful. Keep this momentum, softly.",
        "You made today count.",
        "Another win for you. Take it in.",
        "You are doing the work, and it is working.",
        "Well done. You deserve a moment of peace now.",
        "That is progress, even if it feels quiet.",
    )

    private val reminders: List<String> = listOf(
        "Your heart is waiting. {n} of 3 steps done today. Come and take the next one.",
        "A gentle nudge: today's story and jokes are ready, and so is your next step.",
        "How are you doing? Check in today and let your heart close a little more.",
        "You have {left} days to go. Today's steps are small. Come and explore the app.",
        "A quiet moment for you today. {n} of 3 steps are done.",
        "Your heart rebuild is at {pct}%. Come back and move it forward.",
        "Show up for yourself today. We are here whenever you are ready.",
    )

    fun dailyFor(completedDays: Int): String = daily[completedDays.coerceIn(0, daily.lastIndex)]

    fun afterTaskFor(epochDay: Long): String = afterTask[Math.floorMod(epochDay, afterTask.size.toLong()).toInt()]

    fun reminderFor(epochDay: Long, stepsDone: Int, daysToGo: Int, percent: Int): String =
        reminders[Math.floorMod(epochDay, reminders.size.toLong()).toInt()]
            .replace("{n}", stepsDone.toString()).replace("{left}", daysToGo.toString()).replace("{pct}", percent.toString())

    val doneToday: List<String> = listOf(
        "You finished today's steps. Rest well and come back tomorrow.",
        "Today is complete. Your heart closed a little more.",
        "All three steps are done today. Be proud, and see you tomorrow.",
    )
    fun doneTodayFor(epochDay: Long): String = doneToday[Math.floorMod(epochDay, doneToday.size.toLong()).toInt()]

    val celebration: List<String> = listOf(
        "30 days complete. Your heart is healed, and we are celebrating you today.",
        "Day 2 of celebrating you. Look at what you built in 30 days.",
        "Keep checking in regularly. Your heart is healing, and we are still here.",
    )

    val brokenAgain: List<String> = listOf(
        "Feeling broken again does not erase what you built. You healed once, and you can do it again. I'm with you.",
        "A hard day is not a failed day. Let's rebuild together, one small step at a time.",
        "You already know the way. This time you start with experience and a lot more strength.",
        "Healing comes in waves. This is only another one. We will get through it together.",
        "Thank you for being honest about how you feel. That honesty is where healing starts.",
    )
    fun brokenAgainFor(epochDay: Long): String = brokenAgain[Math.floorMod(epochDay, brokenAgain.size.toLong()).toInt()]

    const val COMPANION_NUDGE =
        "If you want to talk it out live, you can use BQ Sqwad AI Companion. Open BQ Sqwad AI, tap the + inside the chat box (top right) or More features, then choose Companion."
}
