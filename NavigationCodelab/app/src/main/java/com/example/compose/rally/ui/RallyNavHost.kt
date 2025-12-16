package com.example.compose.rally.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.Accounts
import com.example.compose.rally.Bills
import com.example.compose.rally.Overview
import com.example.compose.rally.SingleAccount
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.overview.OverviewScreen

@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier) {

    NavHost(
        navController = navController,
        startDestination = Overview.route,
        modifier = modifier
    ) {
        // builder parameter will be defined here as a graph
        composable(route = Overview.route) {
//                    Overview.screen()
            OverviewScreen(
                onClickSeeAllAccounts = {
                    navController.navigateSingleTopTo(route = Accounts.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(route = Bills.route)
                },
                onAccountClick = { accountType ->
                    // re-factor to private function
//                            navController.navigateSingleTopTo("${SingleAccount.route}/$accountType")
                    navController.navigateToSingleAccount(accountType = accountType)

                }
            )
        }
        composable(route = Accounts.route) {
//                    Accounts.screen()
            AccountsScreen(
                onAccountClick = { accountType ->
                    // re-factor to private function
//                            navController.navigateSingleTopTo("${SingleAccount.route}/$accountType")
                    navController.navigateToSingleAccount(accountType = accountType)
                }
            )
        }
        composable(route = Bills.route) {
//                    Bills.screen()
            BillsScreen()
        }
//                composable(route = SingleAccount.route) {
        composable(
            // move route with args to Single Account
//                    route = "${SingleAccount.route}/{${SingleAccount.accountTypeArg}}",
            route = SingleAccount.routeWithArgs,
            // move arguments list to Single Account
//                    arguments = listOf(
//                        navArgument(SingleAccount.accountTypeArg) {
//                            type = NavType.StringType
//                        }
//                    )
            arguments = SingleAccount.arguments,
            // move deeplinks with args to Single Account
//                    deepLinks = listOf(
//                        navDeepLink {
//                            uriPattern = "rally://${SingleAccount.route}/{${SingleAccount.accountTypeArg}}"
//                        }
//                    )
            deepLinks = SingleAccount.deepLinks
        ) { navBackStackEntry ->

            // Retrieve the passed argument
            val accountType =
                navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)

            // Pass accountType to SingleAccountScreen
            SingleAccountScreen(accountType)
        }
    }
}



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
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo(route = "${SingleAccount.route}/$accountType")
}