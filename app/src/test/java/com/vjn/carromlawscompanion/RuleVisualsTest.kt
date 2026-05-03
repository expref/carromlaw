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
    fun `rules without registered visuals report none`() {
        assertFalse(RuleVisuals.hasVisualFor("33"))
        assertFalse(RuleVisuals.hasVisualFor("A.1"))
        assertFalse(RuleVisuals.hasVisualFor(""))
        assertFalse(RuleVisuals.hasVisualFor("unknown"))
    }

    @Test
    fun `hasVisualFor is case sensitive on rule ids`() {
        assertFalse(RuleVisuals.hasVisualFor("41A"))
        assertFalse(RuleVisuals.hasVisualFor("92 "))
    }
}
