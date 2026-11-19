package com.bqsqwad.heartbreakhealer.feature.heartjourney

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JourneyMessagesTest {
    @Test fun thirtyDifferentDailyMessages() {
        assertEquals(30, JourneyMessages.daily.size)
        assertEquals(30, JourneyMessages.daily.toSet().size)
    }
    @Test fun afterTaskMessageChangesEveryDay() {
        (0L until 60L).forEach { d -> assertNotEquals(JourneyMessages.afterTaskFor(d), JourneyMessages.afterTaskFor(d + 1)) }
    }
    @Test fun reminderTextIsFilledIn() {
        val text = JourneyMessages.reminderFor(0, 2, 12, 40)
        assertTrue(!text.contains("{"))
    }
    @Test fun brokenAgainHasMotivation() { assertTrue(JourneyMessages.brokenAgain.size >= 5) }
}
