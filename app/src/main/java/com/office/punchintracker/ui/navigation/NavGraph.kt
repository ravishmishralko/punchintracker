/**
 * Created by Ravish Mishra on 30 November 2025
 * GitHub: https://github.com/ravishmishralko/punchintracker
 */
package com.office.punchintracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.office.punchintracker.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object PunchIn : Screen("punchin")
    object Route : Screen("route")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToPunchIn = { navController.navigate(Screen.PunchIn.route) },
                onNavigateToRoute = { navController.navigate(Screen.Route.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PunchIn.route) {
            PunchInScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Route.route) {
            RouteScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}