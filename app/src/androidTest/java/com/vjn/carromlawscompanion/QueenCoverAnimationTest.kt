package com.vjn.carromlawscompanion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vjn.carromlawscompanion.ui.animation.QueenCoverAnimation
import com.vjn.carromlawscompanion.ui.animation.QueenCoverBranch
import com.vjn.carromlawscompanion.ui.theme.CarromLawsCompanionTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QueenCoverAnimationTest {

    @Rule
    @JvmField
    val composeTestRule = createComposeRule()

    @Test
    fun animation_renders_with_accessible_label() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                QueenCoverAnimation(autoPlay = false, skipAnimation = false)
            }
        }
        composeTestRule
            .onNodeWithContentDescription(
                "Animated diagram of pocketing and covering the Queen",
                substring = true
            )
            .assertIsDisplayed()
    }

    @Test
    fun branch_a_default_label_is_shown() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                QueenCoverAnimation(autoPlay = false, skipAnimation = false)
            }
        }
        composeTestRule.onNodeWithText("Cover succeeds", substring = true).assertIsDisplayed()
    }

    @Test
    fun branch_a_skip_renders_queen_covered() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                QueenCoverAnimation(autoPlay = false, skipAnimation = true)
            }
        }
        composeTestRule.onNodeWithText("Queen confirmed", substring = true).assertIsDisplayed()
    }

    @Test
    fun branch_b_skip_renders_queen_returned() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                QueenCoverAnimation(
                    autoPlay = false,
                    skipAnimation = true,
                    initialBranch = QueenCoverBranch.MISS
                )
            }
        }
        composeTestRule.onNodeWithText("Queen returns", substring = true).assertIsDisplayed()
    }

    @Test
    fun branch_toggle_switches_between_outcomes() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                QueenCoverAnimation(autoPlay = false, skipAnimation = true)
            }
        }
        composeTestRule.onNodeWithText("Queen confirmed", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Show miss", substring = true).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Queen returns", substring = true).assertIsDisplayed()
    }

    @Test
    fun autoplay_branch_a_reaches_covered() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                QueenCoverAnimation(autoPlay = true, skipAnimation = false)
            }
        }
        composeTestRule.mainClock.advanceTimeBy(8_000L)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Queen confirmed", substring = true).assertIsDisplayed()
    }
}
