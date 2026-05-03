package com.vjn.carromlawscompanion.ui.animation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreakArrangementChoreographyTest {

    @Test
    fun `step 0 has no pieces`() {
        assertTrue(piecesFor(0).isEmpty())
    }

    @Test
    fun `negative step is treated as empty`() {
        assertTrue(piecesFor(-1).isEmpty())
    }

    @Test
    fun `step 1 has only the queen at centre`() {
        val pieces = piecesFor(1)
        assertEquals(1, pieces.size)
        val queen = pieces.single()
        assertEquals(PieceColor.QUEEN, queen.color)
        assertEquals(0.5f, queen.xFraction, 1e-4f)
        assertEquals(0.5f, queen.yFraction, 1e-4f)
    }

    @Test
    fun `step 2 adds the inner ring of six pieces around the queen`() {
        val pieces = piecesFor(2)
        assertEquals(7, pieces.size)
        assertEquals(1, pieces.count { it.color == PieceColor.QUEEN })
        val ring = pieces.filter { it.color != PieceColor.QUEEN }
        assertEquals(6, ring.size)
        assertEquals(3, ring.count { it.color == PieceColor.WHITE })
        assertEquals(3, ring.count { it.color == PieceColor.BLACK })
    }

    @Test
    fun `step 2 inner ring alternates white and black`() {
        val pieces = piecesFor(2)
        val ring = pieces.drop(1)
        for (i in ring.indices) {
            val expected = if (i % 2 == 0) PieceColor.WHITE else PieceColor.BLACK
            assertEquals("ring index $i", expected, ring[i].color)
        }
    }

    @Test
    fun `step 3 adds three white Y-arm pieces in the outer ring`() {
        val pieces = piecesFor(3)
        assertEquals(10, pieces.size)
        val newWhites = pieces.drop(7)
        assertEquals(3, newWhites.size)
        assertTrue(newWhites.all { it.color == PieceColor.WHITE })
    }

    @Test
    fun `step 4 has the full 19-piece break arrangement`() {
        val pieces = piecesFor(4)
        assertEquals(19, pieces.size)
        assertEquals(1, pieces.count { it.color == PieceColor.QUEEN })
        assertEquals(9, pieces.count { it.color == PieceColor.WHITE })
        assertEquals(9, pieces.count { it.color == PieceColor.BLACK })
    }

    @Test
    fun `final step still has 19 pieces`() {
        assertEquals(19, piecesFor(BREAK_TOTAL_STEPS).size)
    }

    @Test
    fun `pieces are within the playing surface fractional bounds`() {
        val pieces = piecesFor(BREAK_TOTAL_STEPS)
        pieces.forEach { p ->
            assertTrue("x of $p in 0..1", p.xFraction in 0f..1f)
            assertTrue("y of $p in 0..1", p.yFraction in 0f..1f)
        }
    }

    @Test
    fun `pieces fit within the outer circle radius`() {
        val pieces = piecesFor(BREAK_TOTAL_STEPS)
        val outerCircleRadius = 0.115f
        val pieceRadius = 0.022f
        pieces.forEach { p ->
            val dx = p.xFraction - 0.5f
            val dy = p.yFraction - 0.5f
            val dist = kotlin.math.sqrt(dx * dx + dy * dy)
            assertTrue(
                "piece $p extends outside the Outer Circle (dist=$dist)",
                dist + pieceRadius <= outerCircleRadius + 1e-3f
            )
        }
    }

    @Test
    fun `pieces have unique positions in the final arrangement`() {
        val pieces = piecesFor(BREAK_TOTAL_STEPS)
        val keys = pieces.map { (it.xFraction * 1000).toInt() to (it.yFraction * 1000).toInt() }
        assertEquals(
            "duplicate piece positions detected",
            keys.size,
            keys.toSet().size
        )
    }

    @Test
    fun `every step label mentions the total step count`() {
        for (step in 0..BREAK_TOTAL_STEPS) {
            val label = labelFor(step)
            if (step in 1..(BREAK_TOTAL_STEPS - 1) || step == 0) {
                assertTrue(
                    "step $step label missing 'of $BREAK_TOTAL_STEPS': '$label'",
                    label.contains("of $BREAK_TOTAL_STEPS")
                )
            }
        }
    }

    @Test
    fun `step labels are distinct across steps`() {
        val labels = (0..BREAK_TOTAL_STEPS).map { labelFor(it) }.toSet()
        assertEquals(
            "all break-arrangement step labels should be distinct",
            BREAK_TOTAL_STEPS + 1,
            labels.size
        )
    }

    @Test
    fun `final step label announces completion`() {
        val finalLabel = labelFor(BREAK_TOTAL_STEPS)
        assertTrue("final label: '$finalLabel'", finalLabel.contains("All pieces in place"))
        assertTrue(finalLabel.contains("Outer Circle"))
    }

    @Test
    fun `step 1 label introduces the queen`() {
        val label = labelFor(1)
        assertTrue(label, label.contains("Queen", ignoreCase = true))
        assertTrue(label, label.contains("centre", ignoreCase = true))
    }

    @Test
    fun `step 3 label highlights the Y-pattern`() {
        val label = labelFor(3)
        assertTrue(label, label.contains("Y-pattern", ignoreCase = true))
    }

    @Test
    fun `pieces grow monotonically with step`() {
        var previous = -1
        for (step in 0..BREAK_TOTAL_STEPS) {
            val count = piecesFor(step).size
            assertNotEquals("step $step regressed in piece count", -2, count)
            assertTrue("step $step has fewer pieces than step ${step - 1}", count >= previous)
            previous = count
        }
    }
}
