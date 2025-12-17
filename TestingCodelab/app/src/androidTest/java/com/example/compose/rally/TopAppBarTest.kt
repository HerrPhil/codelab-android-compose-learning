package com.example.compose.rally

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.compose.rally.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest() {

//        val allScreens = RallyScreen.values().toList()
        val allScreens = RallyScreen.entries.toList()

        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }
//        Thread.sleep(5000)
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }

    @Test
    fun rallyTopAppBarTest_currentLabelExist() {

//        val allScreens = RallyScreen.values().toList()
        val allScreens = RallyScreen.entries.toList()

        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = { },
                currentScreen = RallyScreen.Accounts
            )
        }
//        Thread.sleep(5000)

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

//        composeTestRule
//            .onNodeWithContentDescription(RallyScreen.Accounts.name)
////            .onNodeWithText(RallyScreen.Accounts.name.uppercase())
//            .assertExists()

        composeTestRule
            .onNode(
                matcher = hasText(RallyScreen.Accounts.name.uppercase()) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun rallyAppStateTest_theTextShouldChangeFromOverviewToAccounts() {
        composeTestRule.setContent {
            RallyApp()
        }

        // Before
        composeTestRule
            .onNode(
                matcher = hasParent(
                            hasContentDescription(RallyScreen.Overview.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(label = RallyScreen.Accounts.name)
            .performClick()

        // After
        composeTestRule
            .onNode(
                matcher = hasParent(
                    hasContentDescription(RallyScreen.Accounts.name)
                ),
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun rallyAppStateTest_theTextShouldChangeFromOverviewToBills() {
        composeTestRule.setContent {
            RallyApp()
        }

        // Before
        composeTestRule
            .onNode(
                matcher = hasParent(
                            hasContentDescription(RallyScreen.Overview.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()

        composeTestRule
            .onNodeWithContentDescription(label = RallyScreen.Bills.name)
            .performClick()

        // After
        composeTestRule
            .onNode(
                matcher = hasParent(
                    hasContentDescription(RallyScreen.Bills.name)
                ),
                useUnmergedTree = true
            )
            .assertExists()
    }

}
