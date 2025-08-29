//package com.example.cinesync.uiApp.LoginScreen
//
//import android.content.Context
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performTextInput
//import androidx.navigation.compose.rememberNavController
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.cinesync.uiApp.Login.AuthViewModel
//import com.example.cinesync.uiApp.Login.LoginScreen
//import junit.framework.TestCase.assertTrue
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@RunWith(AndroidJUnit4::class)
//class LoginScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun loginScreen_whenUserInputIsValid_triggersViewModelAction() {
//        val fakeAuthViewModel = FakeAuthViewModel()
//        val usernameToType = "testuser"
//        val passwordToType = "password123"
//
//        composeTestRule.setContent {
//            LoginScreen(
//                authViewModel = fakeAuthViewModel,
//                navController = rememberNavController()
//            )
//        }
//
//        composeTestRule.onNodeWithTag("username_field")
//            .performTextInput(usernameToType)
//
//        composeTestRule.onNodeWithTag("password_field")
//            .performTextInput(passwordToType)
//
//        composeTestRule.onNodeWithTag("login_button")
//            .performClick()
//
//        assertTrue(fakeAuthViewModel.onLoginClickedCalled)
//    }
//
//}