package com.example.musicplayer

import androidx.compose.ui.test.*
import com.example.musicplayer.navigation.NavigationBar
import com.example.musicplayer.screens.AppScreen
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.musicplayer.data.SettingsViewModel
import okhttp3.internal.platform.android.AndroidSocketAdapter.Companion.factory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.startKoin

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @Before
    fun setup() {
        composeTestRule.setContent {
            MainApp()
            App(height = 2340)
        }
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testHomeScreenDefault() {
        composeTestRule.onNodeWithContentDescription("Home button").assertIsSelected()
    }

    @Test
    fun testNavigationButtons() {
        val childrenIds = composeTestRule.onNodeWithContentDescription("Navigation bar").onChild()
            .assertIsDisplayed()
            .onChildren().fetchSemanticsNodes(false).map { it.id }
        val matchers = childrenIds.map { SemanticsMatcher("match by id") { node -> node.id == it } }
        matchers.forEach {
            composeTestRule.onNode(it)
                .performClick()
                .assertIsSelected()
            composeTestRule.waitForIdle()
        }
    }
}


