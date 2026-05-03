package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

object RuleVisuals {
    private val supportedRuleIds: Set<String> = setOf(
        "41a",
        "92"
    )

    fun hasVisualFor(ruleId: String): Boolean = ruleId in supportedRuleIds
}

@Composable
fun VisualFor(ruleId: String) {
    when (ruleId) {
        "41a" -> BreakArrangementAnimation()
        "92" -> QueenCoverAnimation()
    }
}
