/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.rally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.compose.rally.ui.RallyNavHost
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.components.RallyTabRow
import com.example.compose.rally.ui.navigateSingleTopTo
import com.example.compose.rally.ui.overview.OverviewScreen
import com.example.compose.rally.ui.theme.RallyTheme

/**
 * This Activity recreates part of the Rally Material Study from
 * https://material.io/design/material-studies/rally.html
 */
class RallyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RallyApp()
        }
    }
}

@Composable
fun RallyApp() {
    RallyTheme {
        // The old method of manual switching of composables and triggering recomposition
        // to show the new content.
//        var currentScreen: RallyDestination by remember { mutableStateOf(Overview) }

        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        // Fetch the current destination
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Overview
//        val currentScreen= rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Accounts
        Scaffold(
            topBar = {

                // The old method of manual switching of composables and triggering recomposition
                // to show the new content.
//                RallyTabRow(
//                    allScreens = rallyTabRowScreens,
//                    onTabSelected = { screen -> currentScreen = screen },
//                    currentScreen = currentScreen
//                )

                // The Compose Navigation implementation here
                RallyTabRow(
                    allScreens = rallyTabRowScreens,
                    // use the single top extension function to ensure one copy of each destination launches
                    onTabSelected = { newScreen -> navController.navigateSingleTopTo(route = newScreen.route) },
//                    onTabSelected = { newScreen -> navController.navigate(route = newScreen.route) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->

            // The old method of manual switching of composables and triggering recomposition
            // to show the new content.
//            Box(Modifier.padding(innerPadding)) {
//                currentScreen.screen()
//            }


            // Re-factored NavHost into its own composable function

            RallyNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )



            // The Compose Navigation implementation here
//            NavHost(
//                navController = navController,
//                startDestination = Overview.route,
//                modifier = Modifier.padding(innerPadding)
//            ) {
//                // builder parameter will be defined here as a graph
//                composable(route = Overview.route) {
////                    Overview.screen()
//                    OverviewScreen(
//                        onClickSeeAllAccounts = {
//                            navController.navigateSingleTopTo(route = Accounts.route)
//                        },
//                        onClickSeeAllBills = {
//                            navController.navigateSingleTopTo(route = Bills.route)
//                        },
//                        onAccountClick = { accountType ->
//                            // re-factor to private function
////                            navController.navigateSingleTopTo("${SingleAccount.route}/$accountType")
//                            navController.navigateToSingleAccount(accountType = accountType)
//
//                        }
//                    )
//                }
//                composable(route = Accounts.route) {
////                    Accounts.screen()
//                    AccountsScreen(
//                        onAccountClick = { accountType ->
//                            // re-factor to private function
////                            navController.navigateSingleTopTo("${SingleAccount.route}/$accountType")
//                            navController.navigateToSingleAccount(accountType = accountType)
//                        }
//                    )
//                }
//                composable(route = Bills.route) {
////                    Bills.screen()
//                    BillsScreen()
//                }
////                composable(route = SingleAccount.route) {
//                composable(
//                    // move route with args to Single Account
////                    route = "${SingleAccount.route}/{${SingleAccount.accountTypeArg}}",
//                    route = SingleAccount.routeWithArgs,
//                    // move arguments list to Single Account
////                    arguments = listOf(
////                        navArgument(SingleAccount.accountTypeArg) {
////                            type = NavType.StringType
////                        }
////                    )
//                    arguments = SingleAccount.arguments,
//                    // move deeplinks with args to Single Account
////                    deepLinks = listOf(
////                        navDeepLink {
////                            uriPattern = "rally://${SingleAccount.route}/{${SingleAccount.accountTypeArg}}"
////                        }
////                    )
//                    deepLinks = SingleAccount.deepLinks
//                ) { navBackStackEntry ->
//
//                    // Retrieve the passed argument
//                    val accountType =
//                        navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
//
//                    // Pass accountType to SingleAccountScreen
//                    SingleAccountScreen(accountType)
//                }
//            }


        }
    }
}


// Re-factored helper functions to RallyNavHost too.

// launchSingleTop = true: this makes sure there will be at most one copy of a given destination
// on the top of the back stack.

//In Rally app, this would mean that re-tapping the same tab multiple times doesn't launch multiple
// copies of the same destination

// popUpTo(startDestination) { saveState = true } - pop up to the start destination of the graph to
// avoid building up a large stack of destinations on the back stack as you select tabs

// In Rally, this would mean that pressing the back arrow from any destination would pop the entire]
// back stack to Overview

// restoreState = true - determines whether this navigation action should restore any state
// previously saved by PopUpToBuilder.saveState or the popUpToSaveState attribute. Note that, if no
// state was previously saved with the destination ID being navigated to, this has no effect

// In Rally, this would mean that, re-tapping the same tab would keep the previous data and user
// state on the screen without reloading it again
//fun NavHostController.navigateSingleTopTo(route: String) =
//    this.navigate(route) {
//        popUpTo(
//            this@navigateSingleTopTo.graph.findStartDestination().id
//        ) {
//            saveState = true
//        }
//        launchSingleTop = true
//        restoreState = true
//    }
//
//private fun NavHostController.navigateToSingleAccount(accountType: String) {
//    this.navigateSingleTopTo(route = "${SingleAccount.route}/$accountType")
//}