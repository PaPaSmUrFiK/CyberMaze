package com.cybermaze.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cybermaze.features.level01.Level01Screen
import com.cybermaze.features.level_02.Level02Screen
import com.cybermaze.features.level_03.Level03Screen
import com.cybermaze.features.level_04.Level04Screen
import com.cybermaze.features.level_05.Level05Screen
import com.cybermaze.features.level_06.Level06Screen
import com.cybermaze.features.level_08.Level08Screen
import com.cybermaze.features.level_10.Level10Screen
import com.cybermaze.features.levelselect.LevelSelectScreen
import com.cybermaze.features.menu.MainMenuScreen
import com.cybermaze.features.settings.SettingsScreen

/**
 * Main navigation graph for the app.
 * 
 * STUDENTS: Add your level screens here by adding a composable for your level.
 * Example:
 * composable(Screen.Level02.route) { 
 *     Level02Screen(
 *         onExit = { navController.popBackStack() },
 *         onNextLevel = { navController.navigate(Screen.Level03.route) }
 *     )
 * }
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.MainMenu.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Main Menu
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onPlayClick = { navController.navigate(Screen.LevelSelect.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onAboutClick = { navController.navigate(Screen.About.route) }
            )
        }
        
        // Level Select
        composable(Screen.LevelSelect.route) {
            LevelSelectScreen(
                onLevelClick = { levelNumber ->
                    when (levelNumber) {
                        1 -> navController.navigate(Screen.Level01.route)
                        2 -> navController.navigate(Screen.Level02.route)
                        3 -> navController.navigate(Screen.Level03.route)
                        4 -> navController.navigate(Screen.Level04.route)
                        5 -> navController.navigate(Screen.Level05.route)
                        6 -> navController.navigate(Screen.Level06.route)
                        7 -> navController.navigate(Screen.Level07.route)
                        8 -> navController.navigate(Screen.Level08.route)
                        9 -> navController.navigate(Screen.Level09.route)
                        10 -> navController.navigate(Screen.Level10.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Settings
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // About (placeholder)
        composable(Screen.About.route) {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Level 01 - Boot Sector
        composable(Screen.Level01.route) {
            Level01Screen(
                onExit = { 
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = { 
                    navController.navigate(Screen.Level02.route) {
                        popUpTo(Screen.Level01.route) { inclusive = true }
                    }
                }
            )
        }
        
        // STUDENTS: Add your level screens below
        // Level 02-10 placeholders (students will implement these)
        composable(Screen.Level02.route) {
            Level02Screen(
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    navController.navigate(Screen.Level03.route) {
                        popUpTo(Screen.Level02.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Level03.route) {
            Level03Screen (
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    navController.navigate(Screen.Level04.route) {
                        popUpTo(Screen.Level03.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Level04.route) {
            Level04Screen(
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    navController.navigate(Screen.Level05.route) {
                        popUpTo(Screen.Level04.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Level05.route) {
            Level05Screen  (
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    navController.navigate(Screen.Level06.route) {
                        popUpTo(Screen.Level05.route) { inclusive = true }
                    }
                }
            )

//            PlaceholderLevelScreen(
//                levelNumber = 5,
//                onBack = { navController.popBackStack() }
//            )
        }
        
        composable(Screen.Level06.route) {
            Level06Screen (
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    navController.navigate(Screen.Level07.route) {
                        popUpTo(Screen.Level06.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Level07.route) {
            PlaceholderLevelScreen(
                levelNumber = 7,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Level08.route) {
            Level08Screen(
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    navController.navigate(Screen.Level09.route) {
                        popUpTo(Screen.Level08.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Level09.route) {
            PlaceholderLevelScreen(
                levelNumber = 9,
                onBack = { navController.popBackStack() }
            )
        }
        
        // Level 10 - Final Core (boss level: every mechanic + glitch victory anim)
        composable(Screen.Level10.route) {
            Level10Screen(
                onExit = {
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                },
                onNextLevel = {
                    // Final level — no next; route back to level select.
                    navController.popBackStack(Screen.LevelSelect.route, inclusive = false)
                }
            )
        }
    }
}
