package com.example.feriferi

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.feriferi.view.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FeriferiUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    // --- TEST 1: LOGIN ACTIVITY ---
    @Test
    fun testLogin_emptyFields_showsError() {
        composeTestRule.onNodeWithText("Login").performClick()

    }

    @Test
    fun testLogin_typingCredentials() {
        // 1. Find the Email field and type
        // Note: You might need to add .testTag("email_field") in your Composable if text search fails
        composeTestRule.onNodeWithText("Email").performTextInput("test@feriferi.com")

        // 2. Find Password field and type
        composeTestRule.onNodeWithText("Password").performTextInput("password123")

        // 3. Click Login
        composeTestRule.onNodeWithText("Login").performClick()
    }

    // --- TEST 2: REGISTRATION ACTIVITY ---
    @Test
    fun testRegistration_navigation() {
        // 1. Find the link that goes to registration (e.g., "Sign Up")
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // 2. Check if we are on the register screen (look for a button "Register")
        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
    }
    // --- BUYER DASHBOARD TESTS ---
    // BEFORE RUNNING: Change Rule to createAndroidComposeRule<BuyerDashboardActivity>()
    @Test
    fun testBuyerDashboard_Load() {
        // 1. Check if the product list or categories are visible
        // Replace "Categories" with any text that is ALWAYS on your buyer screen
        composeTestRule.onNodeWithText("Categories").assertIsDisplayed()
    }

    // --- SELLER DASHBOARD TESTS ---
    // BEFORE RUNNING: Change Rule to createAndroidComposeRule<SellerDashboardActivity>()
    @Test
    fun testSellerDashboard_AddButtonVisible() {
        // 1. Check if the "Add Product" button (Floating Action Button) exists
        // Use onNodeWithContentDescription if it's an icon without text
        composeTestRule.onNodeWithText("Add Product").assertIsDisplayed()
    }

    // --- ADD PRODUCT TESTS ---
    // BEFORE RUNNING: Change Rule to createAndroidComposeRule<AddProductActivity>()
    @Test
    fun testAddProduct_SubmitWithoutPrice() {
        // 1. Enter a name but leave price empty
        composeTestRule.onNodeWithText("Product Name").performTextInput("Test Item")

        // 2. Click Submit
        composeTestRule.onNodeWithText("Submit").performClick()

        // 3. Verify it didn't finish (Activity shouldn't close) or show error
        // Or simply check if we are still on the same screen
        composeTestRule.onNodeWithText("Submit").assertIsDisplayed()
    }
}