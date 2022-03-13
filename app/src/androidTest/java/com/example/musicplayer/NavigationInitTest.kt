package com.example.musicplayer

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicplayer.navigation.NavigationBar
import com.example.musicplayer.screens.AppScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationInitTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenDefault() {
        composeTestRule.onNodeWithContentDescription("Home button").assertIsSelected()
    }

    @Test
    fun testNavigationInit(){
        val allScreens = AppScreen.values().toList()
        composeTestRule.setContent {
            NavigationBar(
                allScreens = allScreens,
                onTabSelected = {},
                currentScreen = AppScreen.Settings
            )
        }

        composeTestRule.onNodeWithContentDescription("Settings button").assertIsSelected()
    }
}