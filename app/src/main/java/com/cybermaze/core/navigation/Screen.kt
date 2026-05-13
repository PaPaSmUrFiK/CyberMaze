package com.cybermaze.core.navigation

/**
 * Sealed class representing all screens in the app.
 * Students add their level screens here.
 */
sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object LevelSelect : Screen("level_select")
    object Settings : Screen("settings")
    object About : Screen("about")
    
    // Level screens
    object Level01 : Screen("level_01")
    object Level02 : Screen("level_02")
    object Level03 : Screen("level_03")
    object Level04 : Screen("level_04")
    object Level05 : Screen("level_05")
    object Level06 : Screen("level_06")
    object Level07 : Screen("level_07")
    object Level08 : Screen("level_08")
    object Level09 : Screen("level_09")
    object Level10 : Screen("level_10")
}
