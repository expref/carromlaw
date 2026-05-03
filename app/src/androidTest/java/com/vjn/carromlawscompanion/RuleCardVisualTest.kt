package com.vjn.carromlawscompanion

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.vjn.carromlawscompanion.ui.theme.CarromLawsCompanionTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RuleCardVisualTest {

    @Rule
    @JvmField
    val composeTestRule = createComposeRule()

    private fun rule(id: String, title: String = "Sample"): Rule =
        Rule(id = id, title = title, text = "Sample text", tags = emptyList())

    private fun bookmarkManager(): BookmarkManager =
        BookmarkManager(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test
    fun rule_41a_shows_watch_demo_button() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                RuleCard(rule = rule("41a"), bookmarkManager = bookmarkManager())
            }
        }
        composeTestRule.onNodeWithText("Watch demo", substring = true).assertIsDisplayed()
    }

    @Test
    fun rule_92_shows_watch_demo_button() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                RuleCard(rule = rule("92"), bookmarkManager = bookmarkManager())
            }
        }
        composeTestRule.onNodeWithText("Watch demo", substring = true).assertIsDisplayed()
    }

    @Test
    fun rule_without_visual_does_not_show_demo_button() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                RuleCard(rule = rule("33"), bookmarkManager = bookmarkManager())
            }
        }
        composeTestRule.onAllNodesWithText("Watch demo", substring = true).assertCountEquals(0)
    }

    @Test
    fun tapping_watch_demo_expands_animation_for_rule_41a() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                RuleCard(rule = rule("41a"), bookmarkManager = bookmarkManager())
            }
        }
        composeTestRule.onNodeWithText("Watch demo", substring = true).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Step", substring = true).assertIsDisplayed()
    }

    @Test
    fun tapping_watch_demo_expands_animation_for_rule_92() {
        composeTestRule.setContent {
            CarromLawsCompanionTheme {
                RuleCard(rule = rule("92"), bookmarkManager = bookmarkManager())
            }
        }
        composeTestRule.onNodeWithText("Watch demo", substring = true).performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Cover succeeds", substring = true).assertIsDisplayed()
    }
}
