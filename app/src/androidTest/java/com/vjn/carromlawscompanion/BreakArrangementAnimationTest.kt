package com.vjn.carromlawscompanion

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vjn.carromlawscompanion.ui.animation.BreakArrangementAnimation
import com.vjn.carromlawscompanion.ui.theme.CarromLawsCompanionTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BreakArrangementAnimationTest {

    @Rule
    @JvmField
    val composeTestRule = createComposeRule()

    @Test
    fun animation_renders_with_accessible_label() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                BreakArrangementAnimation(autoPlay = false, skipAnimation = false)
            }
        }
        composeTestRule
            .onNodeWithContentDescription(
                "Animated diagram of the break arrangement",
                substring = true
            )
            .assertIsDisplayed()
    }

    @Test
    fun animation_starts_at_first_step_when_paused() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                BreakArrangementAnimation(autoPlay = false, skipAnimation = false)
            }
        }
        composeTestRule.onNodeWithText("Step 1", substring = true).assertIsDisplayed()
    }

    @Test
    fun skip_animation_renders_final_state() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                BreakArrangementAnimation(autoPlay = false, skipAnimation = true)
            }
        }
        composeTestRule
            .onNodeWithText("All pieces in place", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun autoplay_progresses_to_final_step() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                BreakArrangementAnimation(autoPlay = true, skipAnimation = false)
            }
        }
        composeTestRule.mainClock.advanceTimeBy(8_000L)
        composeTestRule.waitForIdle()
        composeTestRule
            .onNodeWithText("All pieces in place", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun restart_button_returns_to_first_step() {
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                BreakArrangementAnimation(autoPlay = true, skipAnimation = false)
            }
        }
        composeTestRule.mainClock.advanceTimeBy(8_000L)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Restart").performClick()
        composeTestRule.mainClock.advanceTimeBy(100L)
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Step 1", substring = true).assertIsDisplayed()
    }
}
