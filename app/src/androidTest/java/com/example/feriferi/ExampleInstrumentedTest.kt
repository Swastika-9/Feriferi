//package com.example.Feriferi
//
//import androidx.compose.ui.test.*
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.feriferi.ForgotPasswordActivity
//import com.example.feriferi.view.LoginActivity
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class FeriferiUITest {
//
//    @get:Rule
////    val composeTestRule = createAndroidComposeRule<LoginActivity>()
//    val composeTestRule = createAndroidComposeRule<ForgotPasswordActivity>()
//
//    // --- TEST 1: LOGIN ACTIVITY ---
//    @Test
//    fun testLogin_emptyFields_showsError() {
//        composeTestRule.onNodeWithText("Login").performClick()
//
//    }
//
//    @Test
//    fun testLogin_typingCredentials() {
//        // 1. Find the Email field and type
//        // Note: You might need to add .testTag("email_field") in your Composable if text search fails
//        composeTestRule.onNodeWithText("Email").performTextInput("test@feriferi.com")
//
//        // 2. Find Password field and type
//        composeTestRule.onNodeWithText("Password").performTextInput("password123")
//
//        // 3. Click Login
//        composeTestRule.onNodeWithText("Login").performClick()
//    }
//
//    // --- TEST 2: REGISTRATION ACTIVITY ---
//    @Test
//    fun testRegistration_navigation() {
//        // 1. Find the link that goes to registration (e.g., "Sign Up")
//        composeTestRule.onNodeWithText("Sign Up").performClick()
//
//        // 2. Check if we are on the register screen (look for a button "Register")
//        composeTestRule.onNodeWithText("Register").assertIsDisplayed()
//    }
//}
//
//
//@Test
//fun testForgotPassword_EmptyEmail() {
//    // 1. Click "Reset Password" (or whatever your button says) without typing anything
//    composeTestRule.onNodeWithText("Reset Password").performClick()
//
//    // 2. Check if an error message appears
//    // Replace "Email is required" with your app's actual error text
//    composeTestRule.onNodeWithText("Email is required").assertIsDisplayed()
//}
//
//@Test
//fun testForgotPassword_ValidEmail() {
//    // 1. Type a valid email
//    // Replace "Enter Email" with the actual placeholder or label text in your UI
//    composeTestRule.onNodeWithText("Enter Email").performTextInput("test@feriferi.com")
//
//    // 2. Click the Reset button
//    composeTestRule.onNodeWithText("Reset Password").performClick()
//
//    // 3. Verify success message (e.g., "Link Sent")
//    // composeTestRule.onNodeWithText("Reset link sent").assertIsDisplayed()
//}