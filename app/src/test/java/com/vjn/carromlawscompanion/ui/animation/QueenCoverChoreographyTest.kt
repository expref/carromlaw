package com.vjn.carromlawscompanion.ui.animation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QueenCoverChoreographyTest {

    private val tol = 1e-4f

    @Test
    fun `step 0 - all pieces visible at their starting positions`() {
        val cover = piecePositionsFor(0, QueenCoverBranch.COVER)
        val miss = piecePositionsFor(0, QueenCoverBranch.MISS)
        assertEquals(cover, miss)
        assertEquals(1f, cover.queenAlpha, tol)
        assertEquals(1f, cover.whiteAlpha, tol)
        assertEquals(1f, cover.strikerAlpha, tol)
        assertEquals(0.5f, cover.queenX, tol)
        assertEquals(0.5f, cover.queenY, tol)
    }

    @Test
    fun `step 1 - striker has advanced toward the queen`() {
        val pos = piecePositionsFor(1, QueenCoverBranch.COVER)
        val pos0 = piecePositionsFor(0, QueenCoverBranch.COVER)
        assertTrue(
            "striker should have moved up the board (smaller y) at step 1",
            pos.strikerY < pos0.strikerY
        )
        assertEquals(1f, pos.queenAlpha, tol)
    }

    @Test
    fun `step 2 - queen is travelling toward a pocket and is still visible`() {
        val pos = piecePositionsFor(2, QueenCoverBranch.COVER)
        assertEquals(1f, pos.queenAlpha, tol)
        assertNotEquals(0.5f, pos.queenX)
        assertNotEquals(0.5f, pos.queenY)
    }

    @Test
    fun `step 3 - queen is pocketed (alpha 0) and white is still on the board`() {
        val cover = piecePositionsFor(3, QueenCoverBranch.COVER)
        val miss = piecePositionsFor(3, QueenCoverBranch.MISS)
        assertEquals(0f, cover.queenAlpha, tol)
        assertEquals(0f, miss.queenAlpha, tol)
        assertEquals(1f, cover.whiteAlpha, tol)
        assertEquals(1f, miss.whiteAlpha, tol)
    }

    @Test
    fun `step 4 - cover branch has white in motion, miss branch leaves white untouched`() {
        val cover = piecePositionsFor(4, QueenCoverBranch.COVER)
        val miss = piecePositionsFor(4, QueenCoverBranch.MISS)
        val rest = piecePositionsFor(0, QueenCoverBranch.COVER)
        assertNotEquals(rest.whiteX, cover.whiteX)
        assertEquals(
            "miss branch must not move the white piece",
            rest.whiteX, miss.whiteX, tol
        )
        assertEquals(rest.whiteY, miss.whiteY, tol)
    }

    @Test
    fun `final step COVER - both queen and white are pocketed`() {
        val pos = piecePositionsFor(QC_TOTAL_STEPS, QueenCoverBranch.COVER)
        assertEquals(0f, pos.queenAlpha, tol)
        assertEquals(0f, pos.whiteAlpha, tol)
    }

    @Test
    fun `final step MISS - queen has returned to centre and is visible again`() {
        val pos = piecePositionsFor(QC_TOTAL_STEPS, QueenCoverBranch.MISS)
        assertEquals(1f, pos.queenAlpha, tol)
        assertEquals(1f, pos.whiteAlpha, tol)
        assertEquals(0.5f, pos.queenX, tol)
        assertEquals(0.5f, pos.queenY, tol)
    }

    @Test
    fun `cover and miss diverge at step 4 and onward`() {
        for (step in 4..QC_TOTAL_STEPS) {
            val cover = piecePositionsFor(step, QueenCoverBranch.COVER)
            val miss = piecePositionsFor(step, QueenCoverBranch.MISS)
            assertNotEquals("step $step branches must differ", cover, miss)
        }
    }

    @Test
    fun `cover and miss are identical for steps before the divergence`() {
        for (step in 0..3) {
            val cover = piecePositionsFor(step, QueenCoverBranch.COVER)
            val miss = piecePositionsFor(step, QueenCoverBranch.MISS)
            assertEquals("step $step branches should match before divergence", cover, miss)
        }
    }

    @Test
    fun `every position is within the playing surface bounds`() {
        for (step in 0..QC_TOTAL_STEPS) {
            for (branch in QueenCoverBranch.entries) {
                val p = piecePositionsFor(step, branch)
                listOf(
                    "queenX" to p.queenX, "queenY" to p.queenY,
                    "whiteX" to p.whiteX, "whiteY" to p.whiteY,
                    "strikerX" to p.strikerX, "strikerY" to p.strikerY
                ).forEach { (name, v) ->
                    assertTrue(
                        "$name=$v out of [0,1] at step=$step branch=$branch",
                        v in 0f..1f
                    )
                }
                listOf(p.queenAlpha, p.whiteAlpha, p.strikerAlpha).forEach { a ->
                    assertTrue("alpha $a out of [0,1] at step=$step branch=$branch", a in 0f..1f)
                }
            }
        }
    }

    @Test
    fun `step labels mention the total step count for in-progress steps`() {
        for (step in 0..(QC_TOTAL_STEPS - 1)) {
            for (branch in QueenCoverBranch.entries) {
                val label = stepLabelFor(step, branch)
                assertTrue(
                    "step=$step branch=$branch label='$label' missing 'of $QC_TOTAL_STEPS'",
                    label.contains("of $QC_TOTAL_STEPS")
                )
            }
        }
    }

    @Test
    fun `final step label COVER announces the cover succeeded`() {
        val label = stepLabelFor(QC_TOTAL_STEPS, QueenCoverBranch.COVER)
        assertTrue(label, label.contains("covered", ignoreCase = true))
    }

    @Test
    fun `final step label MISS references rule 96 - queen returns`() {
        val label = stepLabelFor(QC_TOTAL_STEPS, QueenCoverBranch.MISS)
        assertTrue(label, label.contains("Queen returns", ignoreCase = true))
        assertTrue(label, label.contains("96"))
    }

    @Test
    fun `result label COVER reads as a clear win confirmation`() {
        val label = resultLabelFor(QueenCoverBranch.COVER)
        assertTrue(label, label.contains("Queen confirmed", ignoreCase = true))
        assertTrue(label, label.contains("3 points", ignoreCase = true))
    }

    @Test
    fun `result label MISS describes the queen returning`() {
        val label = resultLabelFor(QueenCoverBranch.MISS)
        assertTrue(label, label.contains("Queen returns", ignoreCase = true))
        assertTrue(label, label.contains("centre", ignoreCase = true))
    }

    @Test
    fun `striker starts on the bottom base lines per Rule 130a`() {
        val pos = piecePositionsFor(0, QueenCoverBranch.COVER)
        val upperBaseLineY = 0.820f
        val lowerBaseLineY = 0.863f
        val strikerRadius = 0.0275f
        assertTrue(
            "striker centre y=${pos.strikerY} should be between the base lines [$upperBaseLineY, $lowerBaseLineY]",
            pos.strikerY in upperBaseLineY..lowerBaseLineY
        )
        assertTrue(
            "striker should overlap upper base line",
            pos.strikerY - strikerRadius <= upperBaseLineY + 1e-3f
        )
        assertTrue(
            "striker should overlap lower base line",
            pos.strikerY + strikerRadius >= lowerBaseLineY - 1e-3f
        )
    }

    @Test
    fun `striker is centred on the bottom edge`() {
        val pos = piecePositionsFor(0, QueenCoverBranch.COVER)
        assertEquals("striker should sit on the lower-side x-centre", 0.5f, pos.strikerX, 1e-3f)
    }

    @Test
    fun `queen travels to the top-right pocket`() {
        val pos = piecePositionsFor(QC_TOTAL_STEPS, QueenCoverBranch.COVER)
        assertTrue(
            "queen final position should be near top-right pocket: x=${pos.queenX}, y=${pos.queenY}",
            pos.queenX > 0.8f && pos.queenY < 0.2f
        )
    }

    @Test
    fun `white travels to the top-left pocket on cover`() {
        val pos = piecePositionsFor(QC_TOTAL_STEPS, QueenCoverBranch.COVER)
        assertTrue(
            "white final position should be near top-left pocket: x=${pos.whiteX}, y=${pos.whiteY}",
            pos.whiteX < 0.2f && pos.whiteY < 0.2f
        )
    }

    @Test
    fun `step labels for in-progress steps differ between branches at and after divergence`() {
        for (step in 4..QC_TOTAL_STEPS) {
            assertNotEquals(
                "step $step labels should differ across branches",
                stepLabelFor(step, QueenCoverBranch.COVER),
                stepLabelFor(step, QueenCoverBranch.MISS)
            )
        }
    }
}
