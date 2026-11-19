package com.bqsqwad.heartbreakhealer.core.domain.journey

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class JourneyMathTest {
    private val none = DayProgress(false, 0, false)

    @Test fun startsAtZero() {
        assertEquals(0, JourneyMath.percent(0, none, false))
        assertEquals(30, JourneyMath.daysToGo(0))
    }
    @Test fun threeTasksMakeOneDay() {
        assertFalse(DayProgress(true, 1, true).complete)
        assertFalse(DayProgress(true, 2, false).complete)
        assertTrue(DayProgress(true, 2, true).complete)
    }
    @Test fun todayFractionMovesThePercentLive() {
        // 0 days + 1 of 3 tasks = 1/90 = 1 percent
        assertEquals(1, JourneyMath.percent(0, DayProgress(true, 0, false), false))
        // 10 days + 2 of 3 tasks = (10 + 2/3)/30 = 35 percent
        assertEquals(35, JourneyMath.percent(10, DayProgress(true, 2, false), false))
    }
    @Test fun todayIsNotCountedTwice() {
        assertEquals(33, JourneyMath.percent(10, DayProgress(true, 2, true), true))
    }
    @Test fun neverShows100UntilDay30IsCounted() {
        assertEquals(99, JourneyMath.percent(29, DayProgress(true, 2, true), false))
        assertEquals(100, JourneyMath.percent(30, none, true))
        assertEquals(0, JourneyMath.daysToGo(30))
    }
    @Test fun fallbackWhenOlderProgramsAreFinished() {
        assertTrue(JourneyMath.rebuildStepDone("2026-11-01", "2026-11-01", false, false))
        assertFalse(JourneyMath.rebuildStepDone(null, "2026-11-01", false, true))
        assertTrue(JourneyMath.rebuildStepDone(null, "2026-11-01", true, true))
        assertFalse(JourneyMath.rebuildStepDone(null, "2026-11-01", true, false))
    }
}
