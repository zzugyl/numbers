package com.babynumbers.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.babynumbers.ui.screens.HomeScreen
import com.babynumbers.ui.screens.StageScreen
import com.babynumbers.ui.screens.NumberDetailScreen
import com.babynumbers.ui.screens.MatchingGameScreen
import com.babynumbers.ui.screens.AutoPlayScreen
import com.babynumbers.ui.screens.GameHomeScreen
import com.babynumbers.ui.screens.ListenGameScreen
import com.babynumbers.ui.screens.ParkingGameScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Stage : Screen("stage/{stageNumber}") {
        fun createRoute(stageNumber: Int) = "stage/$stageNumber"
    }
    object NumberDetail : Screen("number/{number}") {
        fun createRoute(number: Int) = "number/$number"
    }
    object MatchingGame : Screen("matching_game/{stageNumber}") {
        fun createRoute(stageNumber: Int) = "matching_game/$stageNumber"
    }
    object AutoPlay : Screen("autoplay/{stageNumber}") {
        fun createRoute(stageNumber: Int) = "autoplay/$stageNumber"
    }
    object GameHome : Screen("game_home/{stageNumber}") {
        fun createRoute(stageNumber: Int) = "game_home/$stageNumber"
    }
    object ListenGame : Screen("listen_game/{stageNumber}") {
        fun createRoute(stageNumber: Int) = "listen_game/$stageNumber"
    }
    object ParkingGame : Screen("parking_game/{stageNumber}") {
        fun createRoute(stageNumber: Int) = "parking_game/$stageNumber"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    // 导航锁，防止快速连续导航导致的状态混乱
    var isNavigating by remember { mutableStateOf(false) }

    // 监听导航变化，自动释放锁
    androidx.compose.runtime.LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect {
            isNavigating = false
        }
    }

    // 安全返回函数
    val safePopBackStack: () -> Unit = {
        if (!isNavigating && navController.previousBackStackEntry != null) {
            isNavigating = true
            navController.popBackStack()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        // 禁用导航动画以避免快速点击导致的渲染问题
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.Home.route) { backStackEntry ->
            HomeScreen(
                onStageSelected = { stageNumber ->
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.Stage.createRoute(stageNumber))
                    }
                }
            )
        }

        composable(
            route = Screen.Stage.route,
            arguments = listOf(
                navArgument("stageNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val stageNumber = backStackEntry.arguments?.getInt("stageNumber") ?: 1
            StageScreen(
                stageNumber = stageNumber,
                onNumberSelected = { number ->
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.NumberDetail.createRoute(number))
                    }
                },
                onGameHomeSelected = {
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.GameHome.createRoute(stageNumber))
                    }
                },
                onAutoPlay = { stage ->
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.AutoPlay.createRoute(stage))
                    }
                },
                onBack = safePopBackStack
            )
        }

        composable(
            route = Screen.NumberDetail.route,
            arguments = listOf(
                navArgument("number") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val number = backStackEntry.arguments?.getInt("number") ?: 1
            NumberDetailScreen(
                number = number,
                onBack = safePopBackStack
            )
        }

        composable(
            route = Screen.MatchingGame.route,
            arguments = listOf(
                navArgument("stageNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val stageNumber = backStackEntry.arguments?.getInt("stageNumber") ?: 1
            MatchingGameScreen(
                stageNumber = stageNumber,
                onBack = safePopBackStack
            )
        }

        composable(
            route = Screen.AutoPlay.route,
            arguments = listOf(
                navArgument("stageNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val stageNumber = backStackEntry.arguments?.getInt("stageNumber") ?: 1
            AutoPlayScreen(
                stageNumber = stageNumber,
                onBack = safePopBackStack
            )
        }

        composable(
            route = Screen.GameHome.route,
            arguments = listOf(
                navArgument("stageNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val stageNumber = backStackEntry.arguments?.getInt("stageNumber") ?: 1
            GameHomeScreen(
                stageNumber = stageNumber,
                onMatchingGame = {
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.MatchingGame.createRoute(stageNumber))
                    }
                },
                onListenGame = {
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.ListenGame.createRoute(stageNumber))
                    }
                },
                onParkingGame = {
                    if (!isNavigating) {
                        isNavigating = true
                        navController.navigate(Screen.ParkingGame.createRoute(stageNumber))
                    }
                },
                onBack = safePopBackStack
            )
        }

        composable(
            route = Screen.ListenGame.route,
            arguments = listOf(
                navArgument("stageNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val stageNumber = backStackEntry.arguments?.getInt("stageNumber") ?: 1
            ListenGameScreen(
                stageNumber = stageNumber,
                onBack = safePopBackStack
            )
        }

        composable(
            route = Screen.ParkingGame.route,
            arguments = listOf(
                navArgument("stageNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val stageNumber = backStackEntry.arguments?.getInt("stageNumber") ?: 1
            ParkingGameScreen(
                stageNumber = stageNumber,
                onBack = safePopBackStack
            )
        }
    }
}
