package com.vjn.carromlawscompanion.ui.animation

import androidx.compose.runtime.Composable

object RuleVisuals {
    private val supportedRuleIds: Set<String> = setOf(
        // Section I — Standard Equipment (Tier 1)
        "A.1", "A.2", "A.3", "A.4", "A.5", "A.6", "A.7", "B", "C",
        // Section II — Pair / Cannon (Tier 2)
        "16a", "16b",
        // Section II — Stroke (Tier 2)
        "25",
        // Section III + Section XVI base-line strokes (Tier 1)
        "24", "27", "28", "32", "130a", "130b",
        // Section IV — Strike Not Push (Tier 2)
        "33",
        // Section VII — Break (existing + Tier 2)
        "41a", "44",
        // Section XII — Overboard placements (Tier 2)
        "65a", "65b", "65c",
        // Section XIII — Resting positions (Tier 2)
        "67", "68", "69a", "69b", "70a", "70b", "71",
        // Section XV — Queen (existing + Tier 2)
        "92", "113",
        // Section XVI — Striker behaviour (Tier 2)
        "116", "127a", "127b"
    )

    fun hasVisualFor(ruleId: String): Boolean = ruleId in supportedRuleIds
}

@Composable
fun VisualFor(ruleId: String) {
    when (ruleId) {
        // Equipment
        "A.1" -> PlayingSurfaceVisual()
        "A.2" -> FramesVisual()
        "A.3" -> PocketsVisual()
        "A.4" -> BaseLinesVisual()
        "A.5" -> ArrowsVisual()
        "A.6" -> CentreCircleVisual()
        "A.7" -> OuterCircleVisual()
        "B" -> CarrommenVisual()
        "C" -> StrikerVisual()
        // Position / striking geometry
        "24" -> ImaginaryLinesVisual()
        "27" -> SinglesSittingVisual()
        "28" -> DoublesSittingVisual()
        "32" -> BodyBeyondLinesVisual()
        "130a" -> StrikerOnBaseLinesVisual()
        "130b" -> BaseCircleStrokeVisual()
        // Pair / Cannon
        "16a" -> PairVisual()
        "16b" -> CannonVisual()
        // Stroke / striking
        "25" -> StrokeVisual()
        "33" -> StrikeNotPushVisual()
        "44" -> ValidBreakVisual()
        "127a" -> StrikerSlipsVisual()
        "127b" -> SlipNoMovementVisual()
        // Overboard placements
        "65a" -> JumpedPlacementVisual()
        "65b" -> QueenAndCmJumpVisual()
        "65c" -> BothColorsJumpVisual()
        "116" -> StrikerJumpVisual()
        // Resting positions
        "67" -> StandingOnRimVisual()
        "68" -> OverlappingVisual()
        "69a" -> StrikerOnCmVisual()
        "69b" -> PocketMouthFallVisual()
        "70a" -> CmOnStrikerVisual()
        "70b" -> StrikerFallsInVisual()
        "71" -> PrecariousFallVisual()
        "113" -> QueenFallsInVisual()
        // Existing animated visuals
        "41a" -> BreakArrangementAnimation()
        "92" -> QueenCoverAnimation()
    }
}
