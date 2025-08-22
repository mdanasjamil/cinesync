package com.example.cinesync.uiApp.Login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cinesync.R
import com.example.cinesync.uiApp.Navigation.Screens

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
//    loginViewModel: LoginViewModel = hiltViewModel(),
    biometricAuthViewModel: BiometricAuthViewModel = hiltViewModel(),
    onUserLoginReady: () -> Unit = {},
    navController: NavController = rememberNavController()
) {
    val uiState by biometricAuthViewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current


    LaunchedEffect(uiState.loggedIn) {
        if (uiState.loggedIn) {
            // Navigate to the search screen and clear the login screen from the back stack
            navController.navigate(Screens.SearchScreen.name) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Column(
        modifier = modifier.padding(top = 120.dp),
        horizontalAlignment = CenterHorizontally,
    ) {
        OutlinedTextField(
            value = uiState.usernameField,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            placeholder = {
                Text(text = stringResource(id = R.string.username_placeholder))
            },
            onValueChange = {
//                viewModel.updateUsername(it)
            })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.passwordField,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus(force = true)
//                    viewModel.doLogin()
                }
            ),
            visualTransformation = PasswordVisualTransformation(),
            placeholder = {
                Text(text = stringResource(id = R.string.password_placeholder))
            },
            onValueChange = {
//                viewModel.updatePassword(it)
            })


        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                focusManager.clearFocus(force = true)
                biometricAuthViewModel.authenticate(context)
            }
        ) {
            Text(text = stringResource(R.string.login_button_text))
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}