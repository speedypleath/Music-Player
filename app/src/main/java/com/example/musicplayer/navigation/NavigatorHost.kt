package com.example.musicplayer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicplayer.screens.AppScreen
import com.example.musicplayer.screens.HomeBody
import com.example.musicplayer.screens.LibraryBody
import com.example.musicplayer.screens.SettingsBody

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = modifier
    ) {

        composable(AppScreen.Home.name) {
            HomeBody {
                navController.navigate(AppScreen.Library.name)
            }
        }
        composable(AppScreen.Library.name) {
            LibraryBody{
                navController.navigate(AppScreen.Library.name)
            }
        }
        composable(AppScreen.Settings.name) {
            SettingsBody{
                navController.navigate(AppScreen.Settings.name)
            }
        }
//        composable(AppScreen.Library.name) {
//            LibraryBody(playlist = UserData.playlist) { playlist ->
//                navigateToPlaylist(navController = navController, playlist = name)
//            }
//        }
//        composable(Bills.name) {
//            BillsBody(bills = UserData.bills)
//        }
//        val accountsName = Accounts.name
//        composable(
//            route = "$accountsName/{name}",
//            arguments = listOf(
//                navArgument("name") {
//                    type = NavType.StringType
//                }
//            ),
//            deepLinks = listOf(
//                navDeepLink {
//                    uriPattern = "rally://$accountsName/{name}"
//                }
//            ),
//        ) { entry ->
//            val accountName = entry.arguments?.getString("name")
//            val account = UserData.getAccount(accountName)
//            SingleAccountBody(account = account)
//        }
    }
}

//private fun navigateToPlaylist(navController: NavHostController, accountName: String) {
//    navController.navigate("${Accounts.name}/$accountName")
//}