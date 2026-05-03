package com.vjn.carromlawscompanion

import com.vjn.carromlawscompanion.ui.animation.RuleVisuals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RuleVisualsTest {

    @Test
    fun `rule 41a has a visual`() {
        assertTrue(RuleVisuals.hasVisualFor("41a"))
    }

    @Test
    fun `rule 92 has a visual`() {
        assertTrue(RuleVisuals.hasVisualFor("92"))
    }

    @Test
    fun `tier 1 equipment rules have visuals`() {
        listOf("A.1", "A.2", "A.3", "A.4", "A.5", "A.6", "A.7", "B", "C").forEach {
            assertTrue("expected visual for $it", RuleVisuals.hasVisualFor(it))
        }
    }

    @Test
    fun `tier 1 position rules have visuals`() {
        listOf("24", "27", "28", "32", "130a", "130b").forEach {
            assertTrue("expected visual for $it", RuleVisuals.hasVisualFor(it))
        }
    }

    @Test
    fun `tier 2 scenario rules have visuals`() {
        listOf(
            "16a", "16b", "25", "33", "44",
            "65a", "65b", "65c",
            "67", "68", "69a", "69b", "70a", "70b", "71",
            "113", "116", "127a", "127b"
        ).forEach {
            assertTrue("expected visual for $it", RuleVisuals.hasVisualFor(it))
        }
    }

    @Test
    fun `rules without registered visuals report none`() {
        assertFalse(RuleVisuals.hasVisualFor("D"))
        assertFalse(RuleVisuals.hasVisualFor(""))
        assertFalse(RuleVisuals.hasVisualFor("unknown"))
        assertFalse(RuleVisuals.hasVisualFor("42"))
    }

    @Test
    fun `hasVisualFor is case sensitive on rule ids`() {
        assertFalse(RuleVisuals.hasVisualFor("41A"))
        assertFalse(RuleVisuals.hasVisualFor("92 "))
    }
}
